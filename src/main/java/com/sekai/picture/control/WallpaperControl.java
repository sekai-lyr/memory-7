package com.sekai.picture.control;

import com.sekai.picture.model.Category;
import com.sekai.picture.model.Result;
import com.sekai.picture.model.User;
import com.sekai.picture.model.Wallpaper;
import com.sekai.picture.service.CategoryService;
import com.sekai.picture.service.FavoriteService;
import com.sekai.picture.service.WallpaperService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class WallpaperControl {

    @Resource
    private WallpaperService wallpaperService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private FavoriteService favoriteService;

    @PostMapping("/wallpaper/upload")
    public String upload(@ModelAttribute Wallpaper wallpaper,
                         @RequestParam("file") MultipartFile file,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        wallpaper.setUserId(user.getId());
        Result<Wallpaper> result = wallpaperService.upload(wallpaper, file);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return result.isSuccess() ? "redirect:/home" : "redirect:/upload";
    }

    @PostMapping("/category/create")
    public String createCategory(@ModelAttribute Category category,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        category.setUserId(user.getId());
        Result<Category> result = categoryService.create(category);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/upload";
    }

    @PostMapping("/favorite/{wallpaperId}")
    public String favorite(@PathVariable Long wallpaperId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Result<Void> result = favoriteService.toggle(user.getId(), wallpaperId);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/home";
    }

    @PostMapping("/favorite/batch")
    public String batchFavorite(@RequestParam(value = "wallpaperIds", required = false) List<Long> wallpaperIds,
                                @RequestParam("action") String action,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        boolean favorite = "favorite".equals(action);
        Result<Void> result = favoriteService.batchSet(user.getId(), wallpaperIds, favorite);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/home";
    }

    @PostMapping("/wallpaper/{wallpaperId}/update")
    public String updateMeta(@PathVariable Long wallpaperId,
                             @ModelAttribute Wallpaper wallpaper,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        wallpaper.setId(wallpaperId);
        Result<Wallpaper> result = wallpaperService.updateMeta(user.getId(), wallpaper);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/home";
    }

    @PostMapping("/wallpaper/{wallpaperId}/delete")
    public String delete(@PathVariable Long wallpaperId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Result<Void> result = wallpaperService.delete(user.getId(), wallpaperId);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/home";
    }

    @PostMapping("/wallpaper/batch/category")
    public String batchMoveCategory(@RequestParam(value = "wallpaperIds", required = false) List<Long> wallpaperIds,
                                    @RequestParam(value = "categoryId", required = false) Long categoryId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Result<Void> result = wallpaperService.batchMoveCategory(user.getId(), wallpaperIds, categoryId);
        redirectAttributes.addFlashAttribute(result.isSuccess() ? "message" : "error", result.getMessage());
        return "redirect:/home";
    }

    @GetMapping("/download/{wallpaperId}")
    public void download(@PathVariable Long wallpaperId,
                         HttpSession session,
                         HttpServletResponse response) throws IOException {
        User user = getLoginUser(session);
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }
        Wallpaper wallpaper = wallpaperService.getByIdAndUserId(wallpaperId, user.getId());
        if (wallpaper == null) {
            response.sendRedirect("/home");
            return;
        }
        wallpaperService.increaseDownloadCount(wallpaperId, user.getId());
        String fileName = wallpaper.getFileName() == null ? wallpaper.getTitle() + ".jpg" : wallpaper.getFileName();
        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);
        if (wallpaper.getImageUrl() != null && wallpaper.getImageUrl().startsWith("/wallpaper-image/")) {
            Path localFile = Path.of("uploads", "wallpapers",
                    wallpaper.getImageUrl().substring("/wallpaper-image/".length())).normalize();
            if (Files.exists(localFile)) {
                String contentType = Files.probeContentType(localFile);
                response.setContentType(contentType == null ? "application/octet-stream" : contentType);
                Files.copy(localFile, response.getOutputStream());
                return;
            }
        }
        if (wallpaper.getImageUrl() != null && wallpaper.getImageUrl().startsWith("/uploads/wallpapers/")) {
            Path localFile = Path.of(wallpaper.getImageUrl().substring(1)).normalize();
            if (Files.exists(localFile)) {
                String contentType = Files.probeContentType(localFile);
                response.setContentType(contentType == null ? "application/octet-stream" : contentType);
                Files.copy(localFile, response.getOutputStream());
                return;
            }
        }
        response.sendRedirect(wallpaper.getImageUrl());
    }

    private User getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof User user) {
            return user;
        }
        return null;
    }
}
