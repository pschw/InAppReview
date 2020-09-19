package magicnut.android.godotreviewplugin;

import android.app.Activity;
import android.util.Log;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

public class ReviewPlugin extends GodotPlugin {
    public static final String PLUGIN_NAME = "ReviewPlugin";
    Activity activity;

    /**
     * Constructor calling super and setting the activity.
     *
     * @param godot The godot app that instantiates the plugin.
     */
    public ReviewPlugin(Godot godot) {
        super(godot);
        activity = godot;
    }

    @NonNull
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Return all the method names as list that can be called from godot side.
     *
     * @return
     */
    @NonNull
    @Override
    public List<String> getPluginMethods() {
        return Arrays.asList("startInAppReview");
    }

    /**
     * A set of all signals the plugin can emit.
     *
     * @return
     */
    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();

        signals.add(new SignalInfo("review_flow_finished"));
        signals.add(new SignalInfo("review_flow_started"));
        signals.add(new SignalInfo("review_info_request_unsuccessful"));

        return signals;
    }

    public void startInAppReview() {
        activity.runOnUiThread(() -> {
            ReviewManager manager = ReviewManagerFactory.create(activity);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(requestTask -> {
                if (requestTask.isSuccessful()) {
                    Log.d(PLUGIN_NAME, "The review flow has started.");
                    emitSignal("review_flow_started");
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = requestTask.getResult();
                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                    flow.addOnCompleteListener(task -> {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Log.d(PLUGIN_NAME, "The review flow has finished.");
                        emitSignal("review_flow_finished");
                    });
                } else {
                    // There was some problem, continue regardless of the result.
                    Log.d(PLUGIN_NAME, "There was some problem, continue regardless of the result.");
                    emitSignal("review_info_request_unsuccessful");
                }
            });
        });
    }
}
