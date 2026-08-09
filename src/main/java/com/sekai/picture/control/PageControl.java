package com.sekai.picture.control;

import com.sekai.picture.model.GalleryStats;
import com.sekai.picture.model.User;
import com.sekai.picture.model.Wallpaper;
import com.sekai.picture.service.CategoryService;
import com.sekai.picture.service.FavoriteService;
import com.sekai.picture.service.WallpaperService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PageControl {

    @Resource
    private CategoryService categoryService;

    @Resource
    private WallpaperService wallpaperService;

    @Resource
    private FavoriteService favoriteService;

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/home")
    public String home(HttpSession session,
                       Model model,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "favorite", defaultValue = "false") boolean favorite,
                       @RequestParam(value = "original", required = false) Boolean original,
                       @RequestParam(value = "orientation", required = false) String orientation,
                       @RequestParam(value = "colorTone", required = false) String colorTone,
                       @RequestParam(value = "sort", defaultValue = "latest") String sort,
                       @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        int pageSize = 9;
        int total = wallpaperService.countByUserId(user.getId(), categoryId, keyword, favorite, original, orientation, colorTone);
        int totalPages = Math.max((int) Math.ceil(total / (double) pageSize), 1);
        int currentPage = Math.min(Math.max(page, 1), totalPages);
        List<Wallpaper> wallpapers = wallpaperService.listByUserId(user.getId(), categoryId, keyword, favorite,
                original, orientation, colorTone, sort, currentPage, pageSize);

        model.addAttribute("loginUser", user);
        model.addAttribute("categories", categoryService.listByUserId(user.getId()));
        model.addAttribute("colorTones", wallpaperService.listColorTonesByUserId(user.getId()));
        model.addAttribute("wallpapers", wallpapers);
        model.addAttribute("hotWallpapers", wallpaperService.listHotByUserId(user.getId(), 4));
        model.addAttribute("stats", buildStats(user.getId()));
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("favorite", favorite);
        model.addAttribute("original", original);
        model.addAttribute("orientation", orientation == null ? "" : orientation);
        model.addAttribute("colorTone", colorTone == null ? "" : colorTone);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", total);
        return "home";
    }

    @GetMapping("/upload")
    public String upload(HttpSession session, Model model) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("categories", categoryService.listByUserId(user.getId()));
        return "upload";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("categories", categoryService.listByUserId(user.getId()));
        model.addAttribute("hotWallpapers", wallpaperService.listHotByUserId(user.getId(), 8));
        model.addAttribute("colorTones", wallpaperService.listColorTonesByUserId(user.getId()));
        model.addAttribute("stats", buildStats(user.getId()));
        return "dashboard";
    }

    @GetMapping("/random")
    public String random(HttpSession session,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "favorite", defaultValue = "false") boolean favorite,
                         @RequestParam(value = "original", required = false) Boolean original,
                         @RequestParam(value = "orientation", required = false) String orientation,
                         @RequestParam(value = "colorTone", required = false) String colorTone,
                         RedirectAttributes redirectAttributes) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Wallpaper randomWallpaper = wallpaperService.randomByUserId(user.getId(), categoryId, keyword, favorite,
                original, orientation, colorTone);
        if (randomWallpaper == null) {
            redirectAttributes.addFlashAttribute("error", "当前筛选条件下没有可随机的壁纸");
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("spotlightId", randomWallpaper.getId());
        redirectAttributes.addFlashAttribute("message", "随机选中了：" + randomWallpaper.getTitle());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private GalleryStats buildStats(Long userId) {
        GalleryStats stats = new GalleryStats();
        stats.setWallpaperCount(wallpaperService.countByUserId(userId, null, null, false, null, null, null));
        stats.setCategoryCount(categoryService.countByUserId(userId));
        stats.setFavoriteCount(favoriteService.countByUserId(userId));
        stats.setOriginalCount(wallpaperService.countOriginalByUserId(userId));
        return stats;
    }

    private User getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof User user) {
            return user;
        }
        return null;
    }
}
