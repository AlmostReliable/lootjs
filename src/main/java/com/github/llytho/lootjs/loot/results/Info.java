package com.github.llytho.lootjs.loot.results;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Info {

    String transform();

    class Composite implements Info {
        private final Info base;
        protected List<Info> children = new ArrayList<>();

        public Composite(Info base) {
            this.base = base;
        }

        public Composite(String title) {
            this(new TitledInfo(title));
        }

        public Composite(Icon icon, String title) {
            this(new TitledInfo(icon, title));
        }

        @Override
        public String transform() {
            return getBase().transform();
        }

        public Info getBase() {
            return base;
        }

        public Collection<Info> getChildren() {
            return Collections.unmodifiableCollection(children);
        }

        public void addChildren(Info info) {
            children.add(info);
        }
    }

    class TitledInfo implements Info {
        protected final String title;
        @Nullable
        protected Icon icon;

        public TitledInfo(String title) {
            this.title = title;
        }

        public TitledInfo(Icon icon, String title) {
            this(title);
            this.icon = icon;
        }

        public void setIcon(@Nullable Icon icon) {
            this.icon = icon;
        }

        @Override
        public String transform() {
            if (icon != null) {
                return icon + " " + title;
            }

            return title;
        }
    }

    class RowInfo extends TitledInfo {
        private final String name;

        public RowInfo(String name, String text) {
            super(text);
            this.name = name;
        }

        @Override
        public String transform() {
            return String.format("%-14s : %s", name, super.transform());
        }
    }

    class ResultInfo extends TitledInfo {
        public ResultInfo(String title) {
            super(title);
        }

        public void setResult(boolean result) {
            setIcon(result ? Icon.SUCCEED : Icon.FAILED);
        }
    }
}


