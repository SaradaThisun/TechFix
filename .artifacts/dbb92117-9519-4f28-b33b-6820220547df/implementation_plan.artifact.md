# Add images for items in inventory

This plan involves adding an image URL field to the `SparePart` model, updating the inventory item layout to include an `ImageView`, and using Glide to load the images in the `SparePartsAdapter`.

## Proposed Changes

### [Models]
#### [MODIFY] [SparePart.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/models/SparePart.java)
- Add `private String imageUrl;` field.
- Update the constructor to include `imageUrl`.
- Add getter and setter for `imageUrl`.

### [Layouts]
#### [MODIFY] [item_spare_part.xml](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/res/layout/item_spare_part.xml)
- Add an `ImageView` with ID `ivPartImage` to display the part's thumbnail.
- Reorganize the layout to accommodate the image (e.g., placing it to the left of the part details).

### [Adapters]
#### [MODIFY] [SparePartsAdapter.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/adapters/SparePartsAdapter.java)
- In the `VH` (ViewHolder) class, find the `ivPartImage` by ID.
- In `onBindViewHolder`, use Glide to load the `imageUrl` from the `SparePart` object into the `ivPartImage`.
- Handle placeholder and error images.

### [Data]
#### [MODIFY] [MockData.java](file:///C:/Users/Tharindu/Desktop/DW/TechFix-Android/app/src/main/java/com/techfix/app/utils/MockData.java)
- Update mock `SparePart` objects to include sample image URLs (e.g., from Unsplash or a placeholder service).

## Verification Plan

### Automated Tests
- Build the project to ensure there are no compilation errors.
- Run the app and navigate to the Inventory screen.

### Manual Verification
- Verify that images are displayed for each spare part in the inventory list.
- Check that the layout remains clean and responsive on different screen sizes.
- Verify that placeholder/error images are shown if the URL is empty or invalid.
