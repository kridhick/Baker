package com.example.udacity.karthikeyan.baker.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.karthikeyan.baker.Model.Step;
import com.example.udacity.karthikeyan.baker.R;
import com.example.udacity.karthikeyan.baker.Utils.Utilities;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A fragment representing a single RecipeDetail detail screen.
 * This fragment is either contained in a {@link RecipeDetailListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailStepsActivity}
 * on handsets.
 */
public class RecipeDetailStepsFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String STEP_LIST = "StepList";
    public static final String SELECTED_STEP_POSITION = "SelectedStepPosition";
    public static final String RECIPE_NAME = "RecipeName";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    private String mVideoUrl;
    private String mStepShortDesc;
    private String mStepDetailDesc;
    private String mThumbnailUrl;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private int mSelectedPosition;
    private String mRecipeName;
    private ListItemClickListener lOnClickListener;
    private SimpleExoPlayer Player;

    private boolean playWhenReady= true;
    private int currentWindow;
    private long playBackPosition;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut,int clickedItemIndex,  String recipeName);
    }

    public static RecipeDetailStepsFragment newInstance(List<Step> stepList, int clickedItemPosition, String recipeName){
        RecipeDetailStepsFragment recipeDetailStepsFragment = new RecipeDetailStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEP_LIST, (ArrayList<Step>)stepList);
        bundle.putInt(SELECTED_STEP_POSITION, clickedItemPosition);
        bundle.putString(RECIPE_NAME, recipeName);
        recipeDetailStepsFragment.setArguments(bundle);
        return recipeDetailStepsFragment;
    }

    @BindView(R.id.playerView)
    SimpleExoPlayerView PlayerView;

    @BindView(R.id.recipe_step_detail_text)
    TextView DetailDescriptionTextView;

    @BindView(R.id.thumbImage)
    ImageView thumbImage;

    @BindView(R.id.previousStep)
    Button previousStepButton;

    @BindView(R.id.nextStep)
    Button nextStepButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
        Bundle bundle = getArguments();

            lOnClickListener = (ListItemClickListener) getActivity();

            if (bundle != null &&
                bundle.containsKey(STEP_LIST) &&
                bundle.containsKey(SELECTED_STEP_POSITION) &&
                bundle.containsKey(RECIPE_NAME)) {

            mStepList = bundle.getParcelableArrayList(STEP_LIST);
            mSelectedPosition = bundle.getInt(SELECTED_STEP_POSITION);
            mRecipeName = bundle.getString(RECIPE_NAME);

                mVideoUrl = mStepList.get(mSelectedPosition).getVideoURL();
                mStepDetailDesc = mStepList.get(mSelectedPosition).getDescription();
                mStepShortDesc =mStepList.get(mSelectedPosition).getShortDescription();
                mThumbnailUrl = mStepList.get(mSelectedPosition).getThumbnailURL();


        }

        }
        else
        {
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            mSelectedPosition = savedInstanceState.getInt(SELECTED_STEP_POSITION);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME);

            mVideoUrl = mStepList.get(mSelectedPosition).getVideoURL();
            mStepDetailDesc = mStepList.get(mSelectedPosition).getDescription();
            mStepShortDesc =mStepList.get(mSelectedPosition).getShortDescription();
            mThumbnailUrl = mStepList.get(mSelectedPosition).getThumbnailURL();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipedetail_detail, container, false);


        ButterKnife.bind(this, rootView);

        PlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


        DetailDescriptionTextView.setText(mStepDetailDesc);
        DetailDescriptionTextView.setVisibility(View.VISIBLE);



        if(!mThumbnailUrl.isEmpty()){
            Uri builtUri = Uri.parse(mThumbnailUrl).buildUpon().build();
            Picasso.get().load(builtUri).noPlaceholder().error(R.drawable.ic_launcher_background).into(thumbImage);
        }

        if(!mVideoUrl.isEmpty()) {
           initializePlayer();

           if(Utilities.isTablet(getActivity())){
               PlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
               //getActivity().findViewById(R.id.recipedetail_detail_container).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
           }
           else if(Utilities.isInLandscapeMode(getContext())){
               DetailDescriptionTextView.setVisibility(View.GONE);
               PlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
              /* LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) PlayerView.getLayoutParams();
               layoutParams.width = layoutParams.MATCH_PARENT;
               layoutParams.height = layoutParams.MATCH_PARENT;
               PlayerView.setLayoutParams(layoutParams);*/
               maximizePlayerView();
           }
        }
        else
        {
            PlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_black_24dp));
            //PlayerView.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        }



        previousStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mStepList.get(mSelectedPosition).getId() > 0) {
                    if (Player!=null){
                        Player.stop();
                    }
                    lOnClickListener.onListItemClick(mStepList,mStepList.get(mSelectedPosition).getId() - 1,mRecipeName);
                }
                else {
                    Toast.makeText(getActivity(),R.string.MSG_FIST_STEP, Toast.LENGTH_SHORT).show();

                }
            }});

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = mStepList.size()-1;
                if (mStepList.get(mSelectedPosition).getId() < mStepList.get(lastIndex).getId()) {
                    if (Player!=null){
                        Player.stop();
                    }
                    lOnClickListener.onListItemClick(mStepList,mStepList.get(mSelectedPosition).getId() + 1,mRecipeName);
                }
                else {
                    Toast.makeText(getContext(),R.string.MSG_LAST_STEP, Toast.LENGTH_SHORT).show();

                }
            }});

        if(savedInstanceState != null)
        {
            DetailDescriptionTextView.setVisibility(View.INVISIBLE);
            previousStepButton.setVisibility(View.INVISIBLE);
            nextStepButton.setVisibility(View.INVISIBLE);
            PlayerView.setVisibility(View.INVISIBLE);
        }


        return rootView;


    }

 /* @Override
    public void onStart() {
        super.onStart();
        maximizePlayerView();
        initializePlayer();
    }*/

  /*  @Override
    public void onDetach() {
        super.onDetach();
        if(null == Player) initializePlayer();
    }*/



 /*   @Override
    public void onStop() {
        super.onStop();
        if(null != Player) releasePlayer();

    }

    // While resuming from Pause - check the previous instance, if not initialize and maximize.
    @Override
    public void onResume() {
        super.onResume();
        if(null == Player) {
            initializePlayer();
            maximizePlayerView();
        }
    } */

    // On Configuration change - onPause will be invoked first - Ensure to release the player.
    @Override
    public void onPause() {
        super.onPause();
        if(null != Player)
        {
            releasePlayer();
            previousStepButton.setVisibility(View.INVISIBLE);
            nextStepButton.setVisibility(View.INVISIBLE);
            DetailDescriptionTextView.setVisibility(View.INVISIBLE);
            PlayerView.setVisibility(View.INVISIBLE);
        }
    }

    private void initializePlayer(){
        if(Player == null) {

            Player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity())
                    , new DefaultTrackSelector()
                    , new DefaultLoadControl());
            PlayerView.setPlayer(Player);

            Player.seekTo(currentWindow, playBackPosition);

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoUrl)
                    , new DefaultHttpDataSourceFactory("Karthik's Baking App")
                    , new DefaultExtractorsFactory(), null, null);
            Player.prepare(mediaSource);
            Player.setPlayWhenReady(playWhenReady);
        }

    }



    private void releasePlayer(){
        if (Player != null){
            playWhenReady = Player.getPlayWhenReady();
            currentWindow = Player.getCurrentWindowIndex();
            playBackPosition = Player.getCurrentPosition();
            Player.stop();
            Player.release();
            Player = null;
        }
    }

    private void maximizePlayerView() {
        PlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(STEP_LIST,mStepList);
        currentState.putInt(SELECTED_STEP_POSITION,mSelectedPosition);
        currentState.putString(RECIPE_NAME,mRecipeName);
    }
}
