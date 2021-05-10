
Support my work:
**Follow me on [Twitter](https://twitter.com/pascalschwenke) and add me on [LinkedIn](https://www.linkedin.com/in/pascal-schwenke-537a8a169/)!**


# In-App Review plugin for Godot 3.3

This plugin implements the [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review/) for [Godot 3.3](https://godotengine.org/).

See the plugin in action:\
[![Demo video](https://github.com/pschw/InAppReview/blob/main/thumbnail_mini.png?raw=true)](https://youtu.be/yeLkmzhKMUg "Demo video")

## Adding the plugin to Godot 3.3
1. Follow the [official documentation](https://docs.godotengine.org/en/latest/getting_started/workflow/export/android_custom_build.html) to configure, install and enable an Android Custom Build.
2. Download the In-App Review plugin from the release tab.
3. Extract the contents of InAppReviewPlugin.7z to `res://android/plugins`
4. Call the plugin from a godot script (see chapter below).
5. Go to the folder `res://android/build`open the file config.gradle and make sure at least `minSdk : 21` is set.
6. When exporting your game via a preset in `Project>Export...` make sure that `Use Custom Build` and `Review Plugin` is checked.

## Using the plugin in a Godot script
Check if the singleton instance of `ReviewPlugin` is available. Optionally you can connect the signals of the plugin.
```javascript
func start_in_app_review():
   if Engine.has_singleton("ReviewPlugin"):
      review = Engine.get_singleton("ReviewPlugin")
      
      # connect signals - optional!
      review.connect("review_flow_started", self, "review_flow_started")
      review.connect("review_flow_finished", self, "review_flow_finished")
      review.connect("review_info_request_unsuccessful", self, "review_info_request_unsuccessful")
      
      # Try to get a review
      review.startInAppReview()
```
### Signals emitted by the plugin
1. `review_flow_started` **and** `review_info_request_unsuccessful`
After calling startInAppReview() Android requests a ReviewInfo Task. If this is sucessfull the review flow is started and review_flow_started is emitted. In case of failure review_info_request_unsuccessful is emitted.
2. `review_flow_finished`
This signal is emitted when the review flow has finished. The API does not indicate whether the user reviewed or not, or even whether the review dialog was shown. If this puzzles you check out the official documentation of the API.

### Remarks
Make sure to read through the official documentation of [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review/) to learn about quotas or what to expect of the API in general or how it can be tested.
The Plugin is very simple but can be debugged by using the Android Debug Bridge and the tag filter `ReviewPlugin`.
