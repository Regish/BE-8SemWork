
### Pre-requisite:
- Clone the repository on your computer
- Install OpenCV on your computer
	> I'm using Windows,
  > so, I downloaded the OpenCV installation file for Windows
  >
  > At the time of Development (year 2015), version "OpenCV-2.4.10" was used
  >
	> Once the download completes, run the Installation file
	> 
  > It prompts for Extract Location, specify a suitable folder

---

### Project Setup:
1. Open Eclipse
	> I'm using the 
  eclipse Version: Luna Service Release 1a (4.4.1)
	Build id: 20150109-0600
2. Click "**File**", then click "**Import**"
3. "**Import**" window will pop up
4. select "**General**" >> "**Existing Projects into Workspace**"
5. then click "**Next**"
6. Browse to the path where the Repository is stored on Disk
7. Click "**Finish**"
8. You should be able to see the Project Files under "**Package Explorer**"
9. Now, we need to include the OpenCV library as a dependency for our project
10. On the main Eclipse Menu bar, click on "**Window**" >> "**Preferences**"
11. "**Preferences**" window will pop up
12. Select **"Java" >> "Build Path" >> "User Libraries"** and choose "**New**"
13. "**New User Library**" window will pop up
14. Enter a name for the library (e.g., opencv-2.4.10) and click "**OK**"
15. Now, select "**Add External JARs**"
16. Browse to the folder where OpenCV installation was saved
17. Go to the subdirectories **"opencv" >> "build" >> "java"**
18. Select the **opencv jar file** and click "**Open**"
19. After adding the jar, extend it, select "**Native library location**" and press "**Edit**"
20. Select "**External Folder**" and browse to select the folder containing the OpenCV libraries **>> "opencv" >> "build" >> "java" >> "x64"**
21. Click "**OK**"
22. On the Main Eclipse Window, under "**Package Explorer**" right click on the Project folder
23. Choose **"Build Path" >> "Add Libraries"**
24. "**Add Library**" window will pop up
25. Select "**User Library**" and click "**Next**"
26. Select the **available opencv library** and click "**Finish**"

---

### Verify the setup:
1. Open the file "**LoginDemo.java**" and click the "**Run**" button
2. **login window** should pop up.
3. The default credentials are "ADMIN" and "admin".

---
