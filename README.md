
Support my work:
**Follow me on [Twitter](https://twitter.com/pascalschwenke) and add me on [LinkedIn](https://www.linkedin.com/in/pascal-schwenke-537a8a169/)!**


# In-App Review plugin for Godot 3.2.3

This plugin implements the [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review/) for [Godot 3.2.3](https://godotengine.org/). 

See the plugin in action:\
[![Demo video](https://github.com/pschw/GodotConsentPlugin/blob/master/thumbnail_mini.jpg?raw=true)](https://youtu.be/PJ2H8ZK8O_w "Demo video")

## Adding the plugin to Godot 3.2.3
1. Follow the [official documentation](https://docs.godotengine.org/en/latest/getting_started/workflow/export/android_custom_build.html) to configure, install and enable an Android Custom Build.
2. Download the In-App Review plugin from the release tab.
3. Extract the contents of InAppReviewPlugin.7z to `res://android/plugins`
4. Call the plugin from a godot script (see chapter below).
5. When exporting your game via a preset in `Project>Export...` make sure that `Use Custom Build` and `Review Plugin` is checked.

## Using the plugin in a godot script
Check if the singleton instance of `ReviewPlugin` is available. Then connect the signals of the plugin.
```javascript
func check_consent():
   if Engine.has_singleton("ReviewPlugin"):
      review = Engine.get_singleton("ReviewPlugin")
      # connect signals
      review.connect("consent_info_updated",self,"consent_info_updated")
      review.connect("failed_to_update_consent_information",self,"failed_to_update_consent_information")
      review.connect("consent_form_loaded",self,"consent_form_loaded")
      review.start()
```

### Remarks
Make sure to read through the official documentation of [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review/) to learn about quotas or what to expect of the API in general, specifically how it can be tested.
The Plugin is very simple but can be debugged by using the Android Debug Bridge and the tag filter `ReviewPlugin`.
Required min SDK?