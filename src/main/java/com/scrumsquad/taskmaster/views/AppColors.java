package com.scrumsquad.taskmaster.views;

import com.scrumsquad.taskmaster.lib.CommonUtils;
import com.scrumsquad.taskmaster.lib.SwingUtils;

import java.awt.*;

public class AppColors {
    private AppColors() {
    }

    public static final Color transparent = new Color(0,0,0,0);

    public static final Color text = new Color(0x020E06);
    public static final Color background = new Color(0xF0FDF4);
    public static final Color primary = new Color(0x22E55F);
    public static final Color secondary = new Color(0x7CB9F0);
    public static final Color accent = new Color(0x5971EC);

    public static final Color error = new Color(0xed4337);

    public static final Color primaryText = CommonUtils.calculateTextColor(AppColors.primary, AppColors.background, AppColors.text);
    public static final Color secondaryText = CommonUtils.calculateTextColor(AppColors.secondary, AppColors.background, AppColors.text);
    public static final Color accentText = CommonUtils.calculateTextColor(AppColors.accent, AppColors.background, AppColors.text);

    public static final Color primary95 = SwingUtils.withAlpha(primary, 0.95f);
    public static final Color primary90 = SwingUtils.withAlpha(primary, 0.9f);
    public static final Color primary85 = SwingUtils.withAlpha(primary, 0.85f);
    public static final Color primary80 = SwingUtils.withAlpha(primary, 0.8f);
    public static final Color primary75 = SwingUtils.withAlpha(primary, 0.75f);
    public static final Color primary70 = SwingUtils.withAlpha(primary, 0.7f);
    public static final Color primary65 = SwingUtils.withAlpha(primary, 0.65f);
    public static final Color primary60 = SwingUtils.withAlpha(primary, 0.6f);
    public static final Color primary55 = SwingUtils.withAlpha(primary, 0.55f);
    public static final Color primary50 = SwingUtils.withAlpha(primary, 0.5f);
    public static final Color primary45 = SwingUtils.withAlpha(primary, 0.45f);
    public static final Color primary40 = SwingUtils.withAlpha(primary, 0.4f);
    public static final Color primary35 = SwingUtils.withAlpha(primary, 0.35f);
    public static final Color primary30 = SwingUtils.withAlpha(primary, 0.3f);
    public static final Color primary25 = SwingUtils.withAlpha(primary, 0.25f);
    public static final Color primary20 = SwingUtils.withAlpha(primary, 0.2f);
    public static final Color primary15 = SwingUtils.withAlpha(primary, 0.15f);
    public static final Color primary10 = SwingUtils.withAlpha(primary, 0.1f);
    public static final Color primary05 = SwingUtils.withAlpha(primary, 0.05f);

    public static final Color secondary95 = SwingUtils.withAlpha(secondary, 0.95f);
    public static final Color secondary90 = SwingUtils.withAlpha(secondary, 0.9f);
    public static final Color secondary85 = SwingUtils.withAlpha(secondary, 0.85f);
    public static final Color secondary80 = SwingUtils.withAlpha(secondary, 0.8f);
    public static final Color secondary75 = SwingUtils.withAlpha(secondary, 0.75f);
    public static final Color secondary70 = SwingUtils.withAlpha(secondary, 0.7f);
    public static final Color secondary65 = SwingUtils.withAlpha(secondary, 0.65f);
    public static final Color secondary60 = SwingUtils.withAlpha(secondary, 0.6f);
    public static final Color secondary55 = SwingUtils.withAlpha(secondary, 0.55f);
    public static final Color secondary50 = SwingUtils.withAlpha(secondary, 0.5f);
    public static final Color secondary45 = SwingUtils.withAlpha(secondary, 0.45f);
    public static final Color secondary40 = SwingUtils.withAlpha(secondary, 0.4f);
    public static final Color secondary35 = SwingUtils.withAlpha(secondary, 0.35f);
    public static final Color secondary30 = SwingUtils.withAlpha(secondary, 0.3f);
    public static final Color secondary25 = SwingUtils.withAlpha(secondary, 0.25f);
    public static final Color secondary20 = SwingUtils.withAlpha(secondary, 0.2f);
    public static final Color secondary15 = SwingUtils.withAlpha(secondary, 0.15f);
    public static final Color secondary10 = SwingUtils.withAlpha(secondary, 0.1f);
    public static final Color secondary05 = SwingUtils.withAlpha(secondary, 0.05f);

    public static final Color accent95 = SwingUtils.withAlpha(accent, 0.95f);
    public static final Color accent90 = SwingUtils.withAlpha(accent, 0.9f);
    public static final Color accent85 = SwingUtils.withAlpha(accent, 0.85f);
    public static final Color accent80 = SwingUtils.withAlpha(accent, 0.8f);
    public static final Color accent75 = SwingUtils.withAlpha(accent, 0.75f);
    public static final Color accent70 = SwingUtils.withAlpha(accent, 0.7f);
    public static final Color accent65 = SwingUtils.withAlpha(accent, 0.65f);
    public static final Color accent60 = SwingUtils.withAlpha(accent, 0.6f);
    public static final Color accent55 = SwingUtils.withAlpha(accent, 0.55f);
    public static final Color accent50 = SwingUtils.withAlpha(accent, 0.5f);
    public static final Color accent45 = SwingUtils.withAlpha(accent, 0.45f);
    public static final Color accent40 = SwingUtils.withAlpha(accent, 0.4f);
    public static final Color accent35 = SwingUtils.withAlpha(accent, 0.35f);
    public static final Color accent30 = SwingUtils.withAlpha(accent, 0.3f);
    public static final Color accent25 = SwingUtils.withAlpha(accent, 0.25f);
    public static final Color accent20 = SwingUtils.withAlpha(accent, 0.2f);
    public static final Color accent15 = SwingUtils.withAlpha(accent, 0.15f);
    public static final Color accent10 = SwingUtils.withAlpha(accent, 0.1f);
    public static final Color accent05 = SwingUtils.withAlpha(accent, 0.05f);
}
