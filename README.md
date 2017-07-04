# ArcDialSeekBar

![image](https://github.com/esligh/ArcDialSeekBar/raw/master/mmexport1499088584775.jpg)

Android seekbar with arc shape. 

Instead of the normal Android seekbar, you can have a seekbar with the shape of  arc or circle.  All basic parameters are configurable. 
Also it colud be used to create a progress bar.

The only file you need is ArcDialSeekBar.java 

# quick start

#### 1.Add root build.gradle
```
 repositories {
        // ...
        maven { url "https://jitpack.io" }
 }
```
#### 2.Add build.gradle
```
dependencies {
	       compile 'com.github.esligh:ArcDialSeekBar:1.0.0'
	}
```
#### 3.Added to the XML
```
<com.wiget.ext.arcdialseekbar.ArcDialSeekBar
        android:id="@+id/arc_sb_view_2"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_below="@id/arc_sb_view_1"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="20dp"
        android:layout_margin="16dp"
        app:start_angle="135"
        app:end_angle="45"
        app:long_mark_len="15dp"
        app:short_mark_len="8dp"
        app:progress_color="#009688"
        app:text_color="#009688"
        app:text_size="12sp"
        app:inner_divider_count="5"
        app:long_mark_count="8"
        app:show_text="true"
        app:color_mode="single"/>
```

# attributes

* line_stroke_width  
  **the line width for the long and short marks**
* background_color       
  **the arc background color ,default if dark grey** 
* start_angle            
  **start_angle range 0 to 360 clockwise**  
* end_angle             
  **end_angle range 0 to 360  clockwise**
* touch_enable           
  **if false , it can be used as a progress** 
* show_text              
  **if true,show the indicator text**  
* text_color            
  **the color of text** 
* text_size            
  **the size of text** 
* long_mark_count       
  **the total count of long mark**
* inner_divider_count   
  **the value between two long marks.** 
* long_mark_len  
  **the length of the long mark**
* short_mark_len    
  **the length of the short mark**
* color_mode            
  **single or gradient** 
* progress_color        
  **if color mode is single ,this attribute will be valid** .

# License
    Copyright 2015-2019 esligh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
