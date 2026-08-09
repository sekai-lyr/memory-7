CREATE DATABASE IF NOT EXISTS sekai_friend DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sekai_friend;

CREATE TABLE IF NOT EXISTS sekai_picture_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    nick_name VARCHAR(64) NOT NULL,
    avatar_url VARCHAR(500),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sekai_picture_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    cover_url VARCHAR(500),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_category_name (user_id, name),
    KEY idx_category_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sekai_picture_wallpaper (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(120) NOT NULL,
    character_name VARCHAR(120),
    source_name VARCHAR(120),
    tags VARCHAR(255),
    image_url VARCHAR(500) NOT NULL,
    file_name VARCHAR(255),
    file_size BIGINT DEFAULT 0,
    width INT,
    height INT,
    color_tone VARCHAR(40),
    is_original TINYINT(1) NOT NULL DEFAULT 0,
    download_count INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_wallpaper_user (user_id),
    KEY idx_wallpaper_category (category_id),
    KEY idx_wallpaper_keyword (title, character_name, source_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sekai_picture_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    wallpaper_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_wallpaper (user_id, wallpaper_id),
    KEY idx_favorite_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO sekai_picture_user (user_name, password, nick_name, avatar_url)
VALUES ('sekai_wallpaper', '123456', 'Sekai Wallpaper', NULL);

SET @wallpaper_user_id = (SELECT id FROM sekai_picture_user WHERE user_name = 'sekai_wallpaper' LIMIT 1);

INSERT IGNORE INTO sekai_picture_category (user_id, name, description, cover_url) VALUES
(@wallpaper_user_id, 'Healing Daily', 'Soft and bright daily wallpapers', NULL),
(@wallpaper_user_id, 'Cyber Neon', 'Night city and neon light wallpapers', NULL),
(@wallpaper_user_id, 'Stage Idol', 'Live, music and stage moments', NULL),
(@wallpaper_user_id, 'Original Works', 'Self-made wallpapers and drafts', NULL);

INSERT INTO sekai_picture_wallpaper
(user_id, category_id, title, character_name, source_name, tags, image_url, file_name, file_size, width, height, color_tone, is_original)
VALUES
(@wallpaper_user_id, (SELECT id FROM sekai_picture_category WHERE user_id = @wallpaper_user_id AND name = 'Healing Daily' LIMIT 1), 'Blue Rooftop After Rain', 'Miku', 'Project Sekai', 'blue,healing,sky', 'https://images.unsplash.com/photo-1493246507139-91e8fad9978e?auto=format&fit=crop&w=1400&q=80', 'sample-sky.jpg', 0, 1400, 900, 'blue', 0),
(@wallpaper_user_id, (SELECT id FROM sekai_picture_category WHERE user_id = @wallpaper_user_id AND name = 'Cyber Neon' LIMIT 1), 'Neon Train at City Edge', 'Rin', 'Original', 'neon,night,cyber', 'https://images.unsplash.com/photo-1519608487953-e999c86e7455?auto=format&fit=crop&w=1400&q=80', 'sample-neon.jpg', 0, 1400, 900, 'violet', 0),
(@wallpaper_user_id, (SELECT id FROM sekai_picture_category WHERE user_id = @wallpaper_user_id AND name = 'Stage Idol' LIMIT 1), 'Stage Light Moment', 'Kanade', 'Project Sekai', 'stage,music,light', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1400&q=80', 'sample-stage.jpg', 0, 1400, 900, 'gold', 0);
