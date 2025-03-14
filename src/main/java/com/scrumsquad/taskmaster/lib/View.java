package com.scrumsquad.taskmaster.lib;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class View extends Widget {

    private ViewOptions options;

    public ViewOptions getOptions() {
        return new ViewOptions(options);
    }

    protected void setOptions(ViewOptions options) {
        this.options = options;
    }

    /**
     * Override this function if you want to cancel the back navigation.
     */
    public boolean canGoBack() {
        return true;
    }

    public void onLoad() {
    }

    public static class ViewOptions {
        private String headerTitle;
        private Color headerColor;
        private Color headerTextColor;
        private Set<JComponent> headerComponents;

        public ViewOptions() {
            this(null);
        }

        private ViewOptions(ViewOptions options) {
            if (options != null) {
                headerTitle = options.headerTitle;
                headerColor = options.headerColor;
                headerTextColor = options.headerTextColor;
                if (options.headerComponents != null) {
                    headerComponents = new HashSet<>(options.headerComponents);
                } else {
                    headerComponents = new HashSet<>();
                }
            } else {
                headerComponents = new HashSet<>();
            }
        }

        public String getHeaderTitle() {
            return headerTitle;
        }

        public ViewOptions setHeaderTitle(String headerTitle) {
            this.headerTitle = headerTitle;
            return this;
        }

        public Color getHeaderColor() {
            return headerColor;
        }

        public Color getHeaderTextColor() {
            return headerTextColor;
        }

        public ViewOptions setHeaderColor(Color color) {
            headerColor = color;
            if (headerColor != null) {
                headerTextColor = CommonUtils.calculateTextColor(color, Color.white, Color.black);
            }
            return this;
        }

        public Set<JComponent> getHeaderComponents() {
            return new HashSet<>(headerComponents);
        }

        public ViewOptions addHeaderComponent(JComponent component) {
            headerComponents.add(component);
            return this;
        }

        public ViewOptions removeHeaderComponent(JComponent component) {
            headerComponents.remove(component);
            return this;
        }
    }
}
