param(
    [string]$SourceDir = "E:\Picture-memory",
    [string]$UserName = "sekai",
    [string]$Database = "sekai_friend",
    [string]$MysqlUser = "root",
    [string]$MysqlPassword = "123456"
)

$ErrorActionPreference = "Stop"
$OutputEncoding = [System.Text.UTF8Encoding]::new($false)

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$TargetDir = Join-Path $ProjectRoot "uploads\wallpapers\sekai-import"
$ImageExtensions = @(".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp")

function Escape-Sql([string]$Value) {
    if ($null -eq $Value) { return "" }
    return $Value.Replace("\", "\\").Replace("'", "''")
}

function Get-ImageInfo([string]$Path) {
    Add-Type -AssemblyName System.Drawing
    try {
        $img = [System.Drawing.Image]::FromFile($Path)
        try {
            return @{
                Width = [int]$img.Width
                Height = [int]$img.Height
            }
        }
        finally {
            $img.Dispose()
        }
    }
    catch {
        return @{
            Width = $null
            Height = $null
        }
    }
}

function Get-Metadata([string]$Name, [Nullable[int]]$Width, [Nullable[int]]$Height, [int]$Index) {
    $base = [System.IO.Path]::GetFileNameWithoutExtension($Name)
    $lower = $base.ToLowerInvariant()
    $category = "Featured"
    $source = "Picture-memory"
    $character = ""
    $theme = "Sekai Pick"
    $tags = @("anime", "wallpaper", "sekai-import")

    $known = @(
        @{ Key = "plastic"; Category = "Healing Daily"; Source = "Plastic Memories"; Character = "Isla"; Theme = "Plastic Memories" },
        @{ Key = "huiye"; Category = "Romance School"; Source = "Kaguya-sama"; Character = "Kaguya Shinomiya"; Theme = "Love Brain Battle" },
        @{ Key = "tianshi"; Category = "Healing Daily"; Source = "The Angel Next Door"; Character = "Mahiru Shiina"; Theme = "Angel Daily" },
        @{ Key = "girls"; Category = "Featured"; Source = "Anime Girls"; Character = ""; Theme = "Girls Collection" },
        @{ Key = "register"; Category = "Login Background"; Source = "Sekai"; Character = ""; Theme = "Login Background" },
        @{ Key = "login"; Category = "Login Background"; Source = "Sekai"; Character = ""; Theme = "Login Background" },
        @{ Key = "qq"; Category = "Login Background"; Source = "Sekai"; Character = ""; Theme = "Login Background" },
        @{ Key = "screenshot"; Category = "Screenshot"; Source = "Local Screenshot"; Character = ""; Theme = "Dev Screenshot" },
        @{ Key = "starrail"; Category = "Game Character"; Source = "Honkai Star Rail"; Character = ""; Theme = "Stellaron Hunter" },
        @{ Key = "honkai"; Category = "Game Character"; Source = "Honkai Star Rail"; Character = ""; Theme = "Stellaron Hunter" },
        @{ Key = "genshin"; Category = "Game Character"; Source = "Genshin Impact"; Character = ""; Theme = "Genshin Character" },
        @{ Key = "miku"; Category = "Virtual Singer"; Source = "VOCALOID"; Character = "Hatsune Miku"; Theme = "Digital Singer" },
        @{ Key = "rem"; Category = "Isekai Fantasy"; Source = "Re:Zero"; Character = "Rem"; Theme = "Blue Moon" },
        @{ Key = "kirito"; Category = "Fantasy Battle"; Source = "Sword Art Online"; Character = "Kirito"; Theme = "Blue Sword" },
        @{ Key = "asuna"; Category = "Fantasy Battle"; Source = "Sword Art Online"; Character = "Asuna"; Theme = "Flash Sword" },
        @{ Key = "ruby"; Category = "Idol Stage"; Source = "Oshi no Ko"; Character = "Hoshino Ruby"; Theme = "Star Stage" },
        @{ Key = "kafka"; Category = "Game Character"; Source = "Honkai Star Rail"; Character = "Kafka"; Theme = "Stellaron Hunter" }
    )

    foreach ($item in $known) {
        if ($lower.Contains($item.Key.ToLowerInvariant())) {
            $category = $item.Category
            $source = $item.Source
            $character = $item.Character
            $theme = $item.Theme
            break
        }
    }

    if ($category -eq "Featured" -and $null -ne $Width -and $null -ne $Height -and $Height -gt 0) {
        $ratio = [double]$Width / [double]$Height
        if ($ratio -lt 0.72) {
            $category = "Anime Poster"
            $theme = "Vertical Poster"
            $tags += "poster"
        }
        elseif ($ratio -gt 1.18) {
            $category = "Desktop Wide"
            $theme = "Wide Scene"
            $tags += "wide"
        }
        elseif ($ratio -ge 0.72 -and $ratio -le 0.9) {
            $category = "Phone Wallpaper"
            $theme = "Phone Portrait"
            $tags += "phone"
        }
    }

    if ($lower.Contains("weixin") -or $lower.Contains("wechat")) {
        $tags += "wechat"
    }

    if ($category -in @("Phone Wallpaper", "Featured") -and ($Index % 4 -eq 0)) {
        $category = "Beach Summer"
        $theme = "Blue Sky Beach"
        $tags += "summer"
    }
    elseif ($category -in @("Phone Wallpaper", "Featured") -and ($Index % 5 -eq 0)) {
        $category = "Scenery Portrait"
        $theme = "Clouds and Hills"
        $tags += "scenery"
    }

    $tone = "Fresh"
    if ($category -in @("Anime Poster", "Fantasy Battle", "Isekai Fantasy")) { $tone = "Dark" }
    if ($category -in @("Beach Summer", "Desktop Wide")) { $tone = "Bright" }
    if ($category -eq "Idol Stage") { $tone = "Pink Purple" }

    $titleStamp = ""
    if ($base -match "(\d{8})(\d{6})") {
        $titleStamp = " " + $matches[1].Substring(4, 4) + "-" + $matches[2].Substring(0, 4)
    }
    $title = "{0} {1:D3}{2} - {3}" -f $theme, $Index, $titleStamp, $category

    return @{
        Category = $category
        Source = $source
        Character = $character
        Tags = ($tags | Select-Object -Unique) -join ","
        ColorTone = $tone
        Title = $title
    }
}

if (-not (Test-Path -LiteralPath $SourceDir)) {
    throw "Source directory not found: $SourceDir"
}

New-Item -ItemType Directory -Force -Path $TargetDir | Out-Null

$files = Get-ChildItem -LiteralPath $SourceDir -Recurse -File |
    Where-Object { $ImageExtensions -contains $_.Extension.ToLowerInvariant() } |
    Sort-Object FullName

if ($files.Count -eq 0) {
    throw "No image files found in $SourceDir"
}

$categories = @(
    @{ Name = "Featured"; Description = "General imported anime wallpapers." },
    @{ Name = "Phone Wallpaper"; Description = "Portrait wallpapers for phones." },
    @{ Name = "Desktop Wide"; Description = "Wide wallpapers for desktop screens." },
    @{ Name = "Anime Poster"; Description = "Vertical anime poster-style images." },
    @{ Name = "Game Character"; Description = "Game character visuals and fan art." },
    @{ Name = "Beach Summer"; Description = "Blue sky, sea, swimwear, and summer mood." },
    @{ Name = "Healing Daily"; Description = "Soft daily-life, school, and warm images." },
    @{ Name = "Romance School"; Description = "Romance, school life, and youth-themed images." },
    @{ Name = "Fantasy Battle"; Description = "Sword, magic, dark, and battle scenes." },
    @{ Name = "Isekai Fantasy"; Description = "Fantasy adventure and isekai character images." },
    @{ Name = "Idol Stage"; Description = "Stage, performance, pink light, and idol visuals." },
    @{ Name = "Virtual Singer"; Description = "Virtual singer and music-themed images." },
    @{ Name = "Scenery Portrait"; Description = "Character portraits with landscape backgrounds." },
    @{ Name = "Login Background"; Description = "Images suitable for login and register pages." },
    @{ Name = "Screenshot"; Description = "Local screenshots and development assets." }
)

$sqlLines = New-Object System.Collections.Generic.List[string]
$escapedUserName = Escape-Sql $UserName
$sqlLines.Add("SET NAMES utf8mb4;")
$sqlLines.Add("INSERT INTO sekai_picture_user (user_name, password, nick_name) SELECT '$escapedUserName', '123456', 'sekai' WHERE NOT EXISTS (SELECT 1 FROM sekai_picture_user WHERE user_name='$escapedUserName');")
$sqlLines.Add("SET @uid := (SELECT id FROM sekai_picture_user WHERE user_name='$escapedUserName' LIMIT 1);")

foreach ($category in $categories) {
    $name = Escape-Sql $category.Name
    $description = Escape-Sql $category.Description
    $sqlLines.Add("INSERT INTO sekai_picture_category (user_id, name, description) SELECT @uid, '$name', '$description' WHERE NOT EXISTS (SELECT 1 FROM sekai_picture_category WHERE user_id=@uid AND name='$name');")
}

$index = 0
$copied = 0
foreach ($file in $files) {
    $index++
    $info = Get-ImageInfo $file.FullName
    $metadata = Get-Metadata $file.Name $info.Width $info.Height $index
    $safeName = "sekai_import_{0:D4}_{1}{2}" -f $index, ([Guid]::NewGuid().ToString("N").Substring(0, 8)), $file.Extension.ToLowerInvariant()
    $targetPath = Join-Path $TargetDir $safeName
    Copy-Item -LiteralPath $file.FullName -Destination $targetPath -Force
    $copied++

    $imageUrl = "/uploads/wallpapers/sekai-import/$safeName"
    $widthSql = if ($null -eq $info.Width) { "NULL" } else { [string]$info.Width }
    $heightSql = if ($null -eq $info.Height) { "NULL" } else { [string]$info.Height }
    $categoryName = Escape-Sql $metadata.Category
    $title = Escape-Sql $metadata.Title
    $character = Escape-Sql $metadata.Character
    $source = Escape-Sql $metadata.Source
    $tags = Escape-Sql $metadata.Tags
    $tone = Escape-Sql $metadata.ColorTone
    $originalName = Escape-Sql $file.Name
    $url = Escape-Sql $imageUrl
    $fileSize = [int64]$file.Length

    $sqlLines.Add(@"
INSERT INTO sekai_picture_wallpaper
    (user_id, category_id, title, character_name, source_name, tags, image_url, file_name, file_size, width, height, color_tone, is_original, download_count)
SELECT
    @uid,
    (SELECT id FROM sekai_picture_category WHERE user_id=@uid AND name='$categoryName' LIMIT 1),
    '$title',
    NULLIF('$character', ''),
    '$source',
    '$tags',
    '$url',
    '$originalName',
    $fileSize,
    $widthSql,
    $heightSql,
    '$tone',
    0,
    0
WHERE NOT EXISTS (
    SELECT 1 FROM sekai_picture_wallpaper WHERE user_id=@uid AND file_name='$originalName'
);
"@)
}

$sqlPath = Join-Path $env:TEMP ("sekai_picture_import_{0}.sql" -f ([Guid]::NewGuid().ToString("N")))
[System.IO.File]::WriteAllText($sqlPath, ($sqlLines -join [Environment]::NewLine), [System.Text.UTF8Encoding]::new($false))

try {
    Get-Content -LiteralPath $sqlPath -Raw -Encoding UTF8 |
        & mysql "-u$MysqlUser" "-p$MysqlPassword" "--default-character-set=utf8mb4" "-D" $Database
}
finally {
    Remove-Item -LiteralPath $sqlPath -Force -ErrorAction SilentlyContinue
}

$countSql = "SELECT COUNT(*) FROM sekai_picture_wallpaper WHERE user_id=(SELECT id FROM sekai_picture_user WHERE user_name='$escapedUserName' LIMIT 1);"
$total = (& mysql "-u$MysqlUser" "-p$MysqlPassword" "--default-character-set=utf8mb4" "-N" "-B" "-D" $Database "-e" $countSql) | Select-Object -First 1

Write-Host "Scanned: $($files.Count)"
Write-Host "Copied: $copied"
Write-Host "User: $UserName"
Write-Host "Total wallpapers for user: $total"
Write-Host "Target: $TargetDir"
