# Fix App Loading and Crash Issues

The app currently stays on the splash screen indefinitely because it lacks automatic navigation to the login screen when no user session is active. Additionally, there is a potential crash in the registration screen's branch selector logic.

## Proposed Changes

### [Component] Navigation & Splash Flow

#### [MODIFY] [SplashActivity.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/activities/SplashActivity.java)
- Add automatic navigation to `LoginActivity` once the splash progress animation completes (if the user is not already logged in).

### [Component] Registration Logic

#### [MODIFY] [RegisterActivity.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/activities/RegisterActivity.java)
- Refactor `selectBranch` to be more robust and avoid direct `getChildAt` calls which can cause crashes if the layout hierarchy changes slightly.
- Add error handling for resource colors.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to ensure compilation.

### Manual Verification
- Deploy the app to a device/emulator.
- Verify the splash screen automatically transitions to the login screen after the progress bar reaches 100%.
- Navigate to the register screen and verify that branch selection works without crashing.
