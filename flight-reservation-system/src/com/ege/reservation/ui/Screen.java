package com.ege.reservation.ui;

import java.util.Scanner;

// This class is a base clads for all UI screens. Pattern: Template Method
public abstract class Screen {
    protected final UIFactory uiFactory;
    protected final Scanner scanner;

    public Screen(UIFactory uiFactory){
        this.uiFactory = uiFactory;
        this.scanner = new Scanner(System.in);
    }

    // Template method pattern here!!
    public final void display(){
        renderHeader();
        renderContent();
        renderFooter();
        handleInput();

    }


    protected void renderHeader() {
        System.out.println("\n========================================");
        System.out.println("  Online Reservation System");
        System.out.println("========================================");
    }

    protected abstract void renderContent();

    protected void renderFooter() {
        System.out.println("========================================\n");
    }

    protected abstract void handleInput();

    protected void clearScreen() {
        // This doesn't really clear the console in all environments,
        // but it adds visual separation
        System.out.println("\n\n\n\n\n");
    }
}
