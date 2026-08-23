# Implementation Plan - Booking Fragment Improvements

This plan outlines improvements to the `BookingFragment.java` to make the booking process more robust, dynamic, and user-friendly.

## User Review Required

> [!NOTE]
> The dates in the booking process were hardcoded to August 2026. I will change them to be dynamically generated based on the current system date.

## Proposed Changes

### Booking Feature Enhancements

#### [MODIFY] [BookingFragment.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/fragments/BookingFragment.java)
- **Dynamic Date Generation**: Replace hardcoded `DATES` and `DATE_SUBS` with logic that generates the next 4 days (Today, Tomorrow, and the following two days).
- **Address Validation**: Update `canProceed()` to ensure the pickup address is provided if "Courier Pickup" is selected.
- **UI Fix in Location Detection**: Change `view.getRootView().findViewById` to use the fragment's root view directly to avoid potential null pointers or incorrect view references.
- **Dynamic Summary Emoji**: Update `populateSummary()` to display the correct device emoji (📱, 💻, 📒) based on the selected device type.
- **UI Consistency**: Ensure chip styles are correctly applied when pre-filling data from arguments.

## Verification Plan

### Automated Tests
- N/A (Unit tests for date generation logic could be added if requested).

### Manual Verification
1. Open the Booking Fragment.
2. Verify that dates are relative to today (e.g., if today is Aug 23, it should show Today (Aug 23), Tomorrow (Aug 24), etc.).
3. Select "Courier Pickup" and try to continue without an address; verify it shows a toast.
4. Use "Auto-detect nearest branch" and verify the UI updates correctly.
5. Finish the booking and verify the summary shows the correct device emoji.
