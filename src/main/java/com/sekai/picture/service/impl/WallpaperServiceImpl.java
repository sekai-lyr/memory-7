package com.sekai.picture.service.impl;

import com.sekai.picture.dataobject.SekaiPictureCategoryDO;
import com.sekai.picture.dataobject.SekaiPictureWallpaperDO;
import com.sekai.picture.mapper.SekaiPictureCategoryMapper;
import com.sekai.picture.mapper.SekaiPictureFavoriteMapper;
import com.sekai.picture.mapper.SekaiPictureWallpaperMapper;
import com.sekai.picture.model.Result;
import com.sekai.picture.model.Wallpaper;
import com.sekai.picture.service.WallpaperService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class WallpaperServiceImpl implements WallpaperService {

    private static final Path UPLOAD_DIR = Path.of("uploads", "wallpapers");
    private static final DateTimeFormatter DATE_PATH = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private SekaiPictureWallpaperMapper wallpaperMapper;

    @Resource
    private SekaiPictureCategoryMapper categoryMapper;

    @Resource
    private SekaiPictureFavoriteMapper favoriteMapper;

    @Override
    @Transactional
    public Result<Wallpaper> upload(Wallpaper wallpaper, MultipartFile file) {
        if (wallpaper == null || trimToNull(wallpaper.getTitle()) == null) {
            return Result.fail("壁纸标题不能为空");
        }
        if (!canUseCategory(wallpaper.getUserId(), wallpaper.getCategoryId())) {
            return Result.fail("请选择自己的分类");
        }
        if (file == null || file.isEmpty()) {
            return Result.fail("请选择要上传的图片");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            return Result.fail("只能上传图片文件");
        }
        try {
            String originalName = file.getOriginalFilename() == null ? "wallpaper" : file.getOriginalFilename();
            String ext = findExtension(originalName);
            Path dayDir = UPLOAD_DIR.resolve(LocalDateTime.now().format(DATE_PATH));
            Files.createDirectories(dayDir);
            String storedName = UUID.randomUUID() + ext;
            Path target = dayDir.resolve(storedName);
            file.transferTo(target);
            fillImageMeta(wallpaper, target);
            wallpaper.setTitle(wallpaper.getTitle().trim());
            wallpaper.setImageUrl("/wallpaper-image/" + dayDir.getFileName() + "/" + storedName);
            wallpaper.setFileName(originalName);
            wallpaper.setFileSize(file.getSize());
            wallpaper.setDownloadCount(0);
            if (wallpaper.getOriginal() == null) {
                wallpaper.setOriginal(false);
            }
            SekaiPictureWallpaperDO wallpaperDO = new SekaiPictureWallpaperDO(wallpaper);
            wallpaperMapper.insert(wallpaperDO);
            Wallpaper saved = wallpaperMapper.selectByIdAndUserId(wallpaperDO.getId(), wallpaper.getUserId()).convertToWallpaper();
            return Result.ok(saved, "上传成功");
        } catch (IOException e) {
            return Result.fail("图片保存失败：" + e.getMessage());
        }
    }

    @Override
    public Wallpaper getByIdAndUserId(Long id, Long userId) {
        SekaiPictureWallpaperDO wallpaperDO = wallpaperMapper.selectByIdAndUserId(id, userId);
        return wallpaperDO == null ? null : wallpaperDO.convertToWallpaper();
    }

    @Override
    public List<Wallpaper> listByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                                        Boolean original, String orientation, String colorTone, String sort,
                                        int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int offset = (safePage - 1) * pageSize;
        return wallpaperMapper.listByUserId(userId, categoryId, trimToNull(keyword), onlyFavorite,
                        original, normalizeFilter(orientation), trimToNull(colorTone), normalizeSort(sort),
                        offset, pageSize)
                .stream()
                .map(SekaiPictureWallpaperDO::convertToWallpaper)
                .toList();
    }

    @Override
    public int countByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                             Boolean original, String orientation, String colorTone) {
        return wallpaperMapper.countByUserId(userId, categoryId, trimToNull(keyword), onlyFavorite,
                original, normalizeFilter(orientation), trimToNull(colorTone));
    }

    @Override
    public List<Wallpaper> listHotByUserId(Long userId, int limit) {
        return wallpaperMapper.listHotByUserId(userId, Math.max(1, Math.min(limit, 12)))
                .stream()
                .map(SekaiPictureWallpaperDO::convertToWallpaper)
                .toList();
    }

    @Override
    public List<String> listColorTonesByUserId(Long userId) {
        return wallpaperMapper.listColorTonesByUserId(userId);
    }

    @Override
    public Wallpaper randomByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                                    Boolean original, String orientation, String colorTone) {
        SekaiPictureWallpaperDO wallpaperDO = wallpaperMapper.randomByUserId(userId, categoryId,
                trimToNull(keyword), onlyFavorite, original, normalizeFilter(orientation), trimToNull(colorTone));
        return wallpaperDO == null ? null : wallpaperDO.convertToWallpaper();
    }

    @Override
    @Transactional
    public Result<Wallpaper> updateMeta(Long userId, Wallpaper wallpaper) {
        if (wallpaper == null || wallpaper.getId() == null) {
            return Result.fail("壁纸不存在");
        }
        if (trimToNull(wallpaper.getTitle()) == null) {
            return Result.fail("壁纸标题不能为空");
        }
        if (!canUseCategory(userId, wallpaper.getCategoryId())) {
            return Result.fail("请选择自己的分类");
        }
        SekaiPictureWallpaperDO existing = wallpaperMapper.selectByIdAndUserId(wallpaper.getId(), userId);
        if (existing == null) {
            return Result.fail("壁纸不存在或没有权限");
        }
        existing.setTitle(wallpaper.getTitle().trim());
        existing.setCategoryId(wallpaper.getCategoryId());
        existing.setCharacterName(trimToNull(wallpaper.getCharacterName()));
        existing.setSourceName(trimToNull(wallpaper.getSourceName()));
        existing.setTags(trimToNull(wallpaper.getTags()));
        existing.setColorTone(trimToNull(wallpaper.getColorTone()));
        existing.setOriginal(Boolean.TRUE.equals(wallpaper.getOriginal()));
        wallpaperMapper.updateMeta(existing);
        Wallpaper updated = wallpaperMapper.selectByIdAndUserId(wallpaper.getId(), userId).convertToWallpaper();
        return Result.ok(updated, "壁纸信息已更新");
    }

    @Override
    @Transactional
    public Result<Void> delete(Long userId, Long wallpaperId) {
        if (wallpaperId == null) {
            return Result.fail("请选择要删除的壁纸");
        }
        favoriteMapper.deleteByUserIdAndWallpaperId(userId, wallpaperId);
        int affected = wallpaperMapper.deleteByIdAndUserId(wallpaperId, userId);
        if (affected == 0) {
            return Result.fail("壁纸不存在或没有权限");
        }
        return Result.ok(null, "已删除壁纸记录");
    }

    @Override
    @Transactional
    public Result<Void> batchMoveCategory(Long userId, List<Long> wallpaperIds, Long categoryId) {
        if (wallpaperIds == null || wallpaperIds.isEmpty()) {
            return Result.fail("请先选择壁纸");
        }
        if (!canUseCategory(userId, categoryId)) {
            return Result.fail("请选择自己的分类");
        }
        List<Long> ids = wallpaperIds.stream()
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return Result.fail("请先选择壁纸");
        }
        int affected = wallpaperMapper.batchMoveCategory(userId, ids, categoryId);
        return Result.ok(null, "已移动 " + affected + " 张壁纸到新分类");
    }

    @Override
    public int countOriginalByUserId(Long userId) {
        return wallpaperMapper.countOriginalByUserId(userId);
    }

    @Override
    public int sumDownloadCountByUserId(Long userId) {
        Integer count = wallpaperMapper.sumDownloadCountByUserId(userId);
        return count == null ? 0 : count;
    }

    @Override
    public void increaseDownloadCount(Long id, Long userId) {
        wallpaperMapper.increaseDownloadCount(id, userId);
    }

    private void fillImageMeta(Wallpaper wallpaper, Path target) throws IOException {
        BufferedImage image = ImageIO.read(target.toFile());
        if (image != null) {
            wallpaper.setWidth(image.getWidth());
            wallpaper.setHeight(image.getHeight());
        }
    }

    private boolean canUseCategory(Long userId, Long categoryId) {
        if (categoryId == null) {
            return true;
        }
        SekaiPictureCategoryDO categoryDO = categoryMapper.selectByPrimaryKey(categoryId);
        return categoryDO != null && Objects.equals(categoryDO.getUserId(), userId);
    }

    private String findExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return ".jpg";
        }
        return fileName.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeFilter(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            return null;
        }
        return switch (normalized) {
            case "portrait", "landscape", "square" -> normalized;
            default -> null;
        };
    }

    private String normalizeSort(String sort) {
        String normalized = trimToNull(sort);
        if (normalized == null) {
            return "latest";
        }
        return switch (normalized) {
            case "popular", "resolution", "title" -> normalized;
            default -> "latest";
        };
    }
}
