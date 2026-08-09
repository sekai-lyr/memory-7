-- 将所有标题中的"精选二次元壁纸"替换为"Sekai"
-- 例如：精选二次元壁纸 711 -> Sekai 711
UPDATE sekai_picture_wallpaper
SET title = REPLACE(title, '精选二次元壁纸', 'Sekai')
WHERE title LIKE '%精选二次元壁纸%';
