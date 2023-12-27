package com.almostreliable.lootjs.util;

import dev.latvian.mods.kubejs.util.ConsoleJS;

import java.util.ArrayList;
import java.util.List;

public class DebugInfo {

    private final List<Line> lines = new ArrayList<>();
    private int indent = 0;

    public void push() {
        this.indent++;
    }

    public void pop() {
        this.indent--;
        if (this.indent < 0) {
            throw new IllegalStateException("Indentation level is negative");
        }
    }

    public void add(String s) {
        this.lines.add(new Line(this.indent, s));
    }

    public void release() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Line line : lines) {
            sb.append("   ".repeat(Math.max(0, line.indent)));
            sb.append(line.text);
            sb.append("\n");
        }

        ConsoleJS.SERVER.info(sb.toString());
    }

    private record Line(int indent, String text) {

    }
}
