# Smart Daily Expense Tracker

A comprehensive expense tracking application built with modern Android development practices, designed specifically for small business owners to digitize their daily expense management.

## 🏗️ Architecture

This app follows **Clean Architecture** principles with **MVVM** pattern:

- **Data Layer**: Room database, Repository pattern
- **Domain Layer**: Use cases, Business logic
- **Presentation Layer**: Jetpack Compose UI, ViewModels

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt (Dagger)
- **Database**: Room
- **Navigation**: Navigation Compose
- **State Management**: StateFlow & Compose State
- **Charts**: Custom Canvas-based charts
- **Animations**: Compose Animations

## ✨ Features

### Core Features
- **Expense Entry Screen**: Add expenses with title, amount, category, notes, and optional receipt
- **Expense List Screen**: View expenses with filtering by date and category, grouping options
- **Reports Screen**: Visual analytics with pie charts and line graphs for last 7 days
- **Real-time Today's Total**: Live calculation of daily spending

### Advanced Features
- **🎨 Dark/Light Theme Toggle**: Animated theme switching
- **📱 Modern UI/UX**: Material 3 design with smooth animations
- **🔍 Smart Filtering**: Filter by category, group by time or category
- **📊 Interactive Charts**: Custom-built pie charts and line graphs
- **💾 Offline-First**: Local Room database storage
- **🎭 Smooth Animations**: Entry animations, FAB animations, theme transitions
- **✅ Form Validation**: Real-time validation with error messages
- **📈 Export Simulation**: Mock PDF/CSV export functionality

## 📱 Screens

1. **Expense List Screen** (Home)
   - Today's total spending card
   - Filterable expense list
   - Group by category or time
   - Animated FAB for adding expenses

2. **Add Expense Screen**
   - Form with validation
   - Category dropdown
   - Notes field (100 char limit)
   - Receipt image placeholder
   - Loading states and animations

3. **Reports Screen**
   - Summary statistics
   - Category-wise pie chart
   - Daily spending trend line chart
   - Export simulation

## 🎯 Business Value

- **Digitizes** paper-based expense tracking
- **Prevents** lost expense records
- **Provides** instant spending insights
- **Enables** better cash flow understanding
- **Supports** business decision making

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run the app on device/emulator

## 📋 Requirements

- Android Studio Hedgehog or newer
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 2.0.0+

## 🎨 AI Usage Summary

This project extensively leveraged AI tools for:
- **Architecture Design**: Clean architecture setup with Hilt DI
- **UI/UX Enhancement**: Modern Material 3 design patterns
- **Code Generation**: ViewModels, Repository patterns, Use cases
- **Animation Implementation**: Smooth transitions and micro-interactions
- **Chart Creation**: Custom Canvas-based visualization components

## 🏆 Key Achievements

- ✅ Full Clean Architecture implementation
- ✅ Modern Jetpack Compose UI
- ✅ Comprehensive form validation
- ✅ Custom chart visualizations
- ✅ Smooth animations throughout
- ✅ Dark/Light theme support
- ✅ Offline-first data persistence
- ✅ Professional UX patterns

## 📸 Screenshots

*Screenshots will be available after building and testing the application*

---

**Built with ❤️ using AI-first development approach**
