package com.gameball.androidx;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.request.Action;
import com.gameball.androidx.model.request.GenerateOTPBody;
import com.gameball.androidx.model.request.GetPlayerBalanceBody;
import com.gameball.androidx.model.request.HoldPointBody;
import com.gameball.androidx.model.request.PlayerInfoBody;
import com.gameball.androidx.model.request.PlayerRegisterRequest;
import com.gameball.androidx.model.request.RedeemPointBody;
import com.gameball.androidx.model.request.ReferralBody;
import com.gameball.androidx.model.request.ReverseHeldPointsbody;
import com.gameball.androidx.model.request.RewardPointBody;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.HoldPointsResponse;
import com.gameball.androidx.model.response.NotificationBody;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.model.response.PlayerBalanceResponse;
import com.gameball.androidx.model.response.PlayerRegisterResponse;
import com.gameball.androidx.network.Callback;
import com.gameball.androidx.network.Network;
import com.gameball.androidx.network.api.GameBallApi;
import com.gameball.androidx.network.profileRemote.ProfileRemoteProfileDataSource;
import com.gameball.androidx.network.transactionRemote.TransactionRemoteDataSource;
import com.gameball.androidx.utils.Constants;
import com.gameball.androidx.utils.DialogManager;
import com.gameball.androidx.views.GameBallMainActivity;
import com.gameball.androidx.views.largeNotification.LargeNotificationActivity;
import com.gameball.androidx.views.mainContainer.MainContainerFragment;
import com.gameball.androidx.views.popupNotificationView.PopupNotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ahmed Abdelmoneam Abdelfattah on 8/23/2018.
 */
public class GameBallApp {
    private static final String TAG = GameBallApp.class.getSimpleName();
    private static final String MAIN_ACTIVITY_ACTION = "GAME_BALL_MAIN_ACTIVITY";
    private static final String TAG_GAMEBALL_PROFILE_DIALOG = "gameball_profile_dialog";
    private static GameBallApp ourInstance;
    private String APPLICATION_ID = null;
    private String API_KEY = null;
    private String SENDER_ID = null;
    private Context mContext;
    private FirebaseApp clientFirebaseApp;
    private String mClientID;
    private String mPlayerUniqueId;
    private Integer mPlayerTypeID;
    private int mNotificationIcon;
    private String mDeviceToken;
    private GameBallApi gameBallApi;
    private TransactionRemoteDataSource transactionRemoteDataSource;
    private ProfileRemoteProfileDataSource profileRemoteProfileDataSource;


    private GameBallApp(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
            gameBallApi = Network.getInstance().getGameBallApi();
            SharedPreferencesUtils.init(mContext, new Gson());
            transactionRemoteDataSource = TransactionRemoteDataSource.getInstance();
            profileRemoteProfileDataSource = ProfileRemoteProfileDataSource.getInstance();
            SharedPreferencesUtils.getInstance().putClientBotSettings(null);
        }
    }

    public static GameBallApp getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new GameBallApp(context);
        }
        return ourInstance;
    }

    private void registerDevice(@Nullable PlayerAttributes playerAttributes, final Callback<PlayerRegisterResponse> callback) {

        if (mPlayerUniqueId == null || mClientID == null) {
            return;
        }

        SharedPreferencesUtils.getInstance().putClientId(mClientID);
        SharedPreferencesUtils.getInstance().putPlayerUniqueId(mPlayerUniqueId);

        PlayerRegisterRequest registerDeviceRequest = new PlayerRegisterRequest();
        registerDeviceRequest.setPlayerUniqueID(mPlayerUniqueId);
        if (mPlayerTypeID != -1) {
            registerDeviceRequest.setPlayerTypeID(mPlayerTypeID);
            SharedPreferencesUtils.getInstance().putPlayerTypeID(mPlayerTypeID);
        }

        if (mDeviceToken != null) {
            registerDeviceRequest.setDeviceToken(mDeviceToken);
            SharedPreferencesUtils.getInstance().putDeviceToken(mDeviceToken);
        }

        if (playerAttributes != null)
            registerDeviceRequest.setPlayerAttributes(playerAttributes);

        gameBallApi.registrationPlayer(registerDeviceRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BaseResponse<PlayerRegisterResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BaseResponse<PlayerRegisterResponse> playerRegisterResponseBaseResponse) {
                        if (callback != null)
                            callback.onSuccess(playerRegisterResponseBaseResponse.getResponse());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null)
                            callback.onError(e);
                    }
                });
    }

    private void getBotSettings() {
        gameBallApi.getBotSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BaseResponse<ClientBotSettings>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("ClientBotSettings","fetching Client settings");
                    }

                    @Override
                    public void onSuccess(BaseResponse<ClientBotSettings> clientBotSettingsBaseResponse) {
                        SharedPreferencesUtils.getInstance().
                                putClientBotSettings(clientBotSettingsBaseResponse.getResponse());
                        initializeFirebase(null, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bot_settings_error", e.getMessage());
                    }
                });
    }

    public void init(@NonNull String clientID, String PlayerUniqueId, int playerTypeID,
                     @DrawableRes int notificationIcon) {
        this.mClientID = clientID;
        this.mPlayerUniqueId = PlayerUniqueId;
        this.mPlayerTypeID = playerTypeID;
        mNotificationIcon = notificationIcon;

        SharedPreferencesUtils.getInstance().putClientId(clientID);
        getBotSettings();
    }

    public void init(String clientID, String playerUniqueId, @DrawableRes int notificationIcon) {
        init(clientID, playerUniqueId, -1, notificationIcon);
    }

    public void init(String clientID, @DrawableRes int notificationIcon) {
        init(clientID, null, -1, notificationIcon);
    }

    private void initializeFirebase(final PlayerAttributes playerAttributes,
                                    final Callback<PlayerRegisterResponse> callback) {

        if (mPlayerUniqueId != null && !mPlayerUniqueId.trim().isEmpty()) {

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    mDeviceToken = task.getResult().getToken();
                    registerDevice(playerAttributes, callback);
                }
            });
        }
    }


    public void registerPlayer(@NonNull String playerUniqueId,
                               @NonNull Callback<PlayerRegisterResponse> callback) {
        registerPlayer(playerUniqueId, -1, null, callback);
    }

    public void registerPlayer(@NonNull String playerUniqueId, PlayerAttributes playerAttributes,
                               @NonNull Callback<PlayerRegisterResponse> callback) {
        registerPlayer(playerUniqueId, -1, playerAttributes, callback);
    }

    public void registerPlayer(@NonNull String playerUniqueId, int playerTypeID,
                               @NonNull Callback<PlayerRegisterResponse> callback) {
        registerPlayer(playerUniqueId, playerTypeID, null, callback);
    }

    public void registerPlayer(@NonNull String playerUniqueId, int playerTypeID, PlayerAttributes playerAttributes,
                               @NonNull Callback<PlayerRegisterResponse> callback) {
        if (!playerUniqueId.trim().isEmpty()) {
            mPlayerUniqueId = playerUniqueId;
            mPlayerTypeID = playerTypeID;

            initializeFirebase(playerAttributes, callback);
        } else {
            Log.e(TAG, "Player registration: PlayerUniqueId cannot be empty");
        }
    }

    public void editPlayerAttributes(@NonNull PlayerAttributes playerAttributes, Callback callback) {
        PlayerInfoBody body = new PlayerInfoBody(playerAttributes);

        profileRemoteProfileDataSource.initializePlayer(body)
                .retry()
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("add_player_info", "success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("add_player_info", e.getMessage());
                    }
                });
    }

    private void sendNotification(final NotificationBody messageBody) {

        final String channelId = mContext.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, channelId)
                        .setContentTitle(messageBody.getTitle())
                        .setContentText(messageBody.getTitle())
                        .setAutoCancel(true)
                        .setSmallIcon(mNotificationIcon)
                        .setSound(defaultSoundUri);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent notificationIntent;

                switch (messageBody.getType()) {
                    case NotificationBody.SMALL_TOAST:
//                        notificationIntent = new Intent(mContext, SmallNotificationActivity.class);
                        DialogManager.addNotificationToQue(mContext,messageBody);
                        break;
                    case NotificationBody.LARGE_TOAST:
                        notificationIntent = new Intent(mContext, PopupNotificationActivity.class);
                        notificationIntent.putExtra(Constants.NOTIFICATION_BODY, messageBody);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(notificationIntent);
                        break;
                    case NotificationBody.POPUP:
                    default:
                        notificationIntent = new Intent(mContext, LargeNotificationActivity.class);
                        notificationIntent.putExtra(Constants.NOTIFICATION_BODY, messageBody);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(notificationIntent);
                        break;

                }
            }
        }, 100);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Game Ball Demo",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(999999999 /* ID of notification */, notificationBuilder.build());
    }

    public boolean isGameBallNotification(RemoteMessage remoteMessage) {
        if (remoteMessage != null && Boolean.valueOf(remoteMessage.getData().get("isGB"))
                && remoteMessage.getNotification() != null) {

            Map<String, String> notificationData = remoteMessage.getData();
            NotificationBody notificationBody = new NotificationBody();
            notificationBody.setTitle(notificationData.get("title"));
            notificationBody.setBody(notificationData.get("body"));
            notificationBody.setIcon(notificationData.get("icon"));
            notificationBody.setType(notificationData.get("type"));

            sendNotification(notificationBody);
            return true;
        }
        return false;
    }

    /**
     * Use Show profile method where ever you like.
     * It just shows the profile of the user that contains the user details, user achievements and the leader board.
     * basically call this method when ever you want to allow the user to see his profile.
     *
     * @param activity if you are using an activity send an instance of the activity to be able to show the profile
     */
    public void showProfile(AppCompatActivity activity) throws Exception {
        if (SharedPreferencesUtils.getInstance().getPlayerUniqueId() == null) {
            throw new RuntimeException(TAG + ": User is not logged in yet!");
        }

        showProfile(activity.getSupportFragmentManager());
    }

    /**
     * It just shows the profile of the user that contains the user details, user achievements and the leader board.
     * basically call this method when ever you want to allow the user to see his profile.
     *
     * @param fragment if you are using a fragment send an instance of the fragment to be able to show the profile
     */
    public void showProfile(Fragment fragment) throws Exception {
        if (SharedPreferencesUtils.getInstance().getPlayerUniqueId() == null) {
            throw new RuntimeException(TAG + ": User is not logged in yet!");
        }

        showProfile(fragment.getChildFragmentManager());
    }

    private void showProfile(final FragmentManager fragmentManager) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ClientBotSettings clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
                return clientBotSettings != null;
            }
        }).flatMap(new Function<Boolean, ObservableSource<ClientBotSettings>>() {
            @Override
            public ObservableSource<ClientBotSettings> apply(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    return Observable.just(SharedPreferencesUtils.getInstance().getClientBotSettings());
                }
                return gameBallApi.getBotSettings().flatMapObservable(
                        new Function<BaseResponse<ClientBotSettings>,
                                ObservableSource<? extends ClientBotSettings>>() {
                            @Override
                            public ObservableSource<? extends ClientBotSettings> apply(BaseResponse<ClientBotSettings> clientBotSettingsBaseResponse) throws Exception {
                                return Observable.just(clientBotSettingsBaseResponse.getResponse());
                            }
                        });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClientBotSettings>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ClientBotSettings clientBotSettings) {
                        SharedPreferencesUtils.getInstance().putClientBotSettings(clientBotSettings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(mContext, GameBallMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
    }

    public void showProfile(final Callback<Fragment> callback) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ClientBotSettings clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
                return clientBotSettings != null;
            }
        }).flatMap(new Function<Boolean, ObservableSource<ClientBotSettings>>() {
            @Override
            public ObservableSource<ClientBotSettings> apply(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    return Observable.just(SharedPreferencesUtils.getInstance().getClientBotSettings());
                }
                return gameBallApi.getBotSettings().flatMapObservable(
                        new Function<BaseResponse<ClientBotSettings>,
                                ObservableSource<? extends ClientBotSettings>>() {
                            @Override
                            public ObservableSource<? extends ClientBotSettings> apply(BaseResponse<ClientBotSettings> clientBotSettingsBaseResponse) throws Exception {
                                return Observable.just(clientBotSettingsBaseResponse.getResponse());
                            }
                        });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClientBotSettings>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ClientBotSettings clientBotSettings) {
                        SharedPreferencesUtils.getInstance().putClientBotSettings(clientBotSettings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(new MainContainerFragment());
                    }
                });
    }

    /**
     * use AddAction when ever you want to trigger that an action is done by the user.
     *
     * @param action the method requires and action object that requires the challengeApiId
     *               another two paramaeters are optional.
     *               amount: is needed if the challenge is amount based
     *               playerCategoryID: us needed if you have multi users categories(default value is 0)
     */
    public void addAction(@NonNull Action action, final Callback callback) {

        Log.i("action", new Gson().toJson(action));
        gameBallApi.addNewAtion(action).
                subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("action:", "action successfull");
                        callback.onSuccess(new Object());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("action:", e.getMessage());
                        callback.onError(e);
                    }
                });
    }

    private void generateOTP(GenerateOTPBody body, final Callback callback) {
        transactionRemoteDataSource.generateOtp(body)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void rewardPoints(RewardPointBody body, final Callback callback) {
        transactionRemoteDataSource.rewardPoints(body)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void holdPoints(HoldPointBody body, final Callback<HoldPointsResponse> callback) {
        transactionRemoteDataSource.holdPoints(body)
                .subscribe(new SingleObserver<BaseResponse<HoldPointsResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BaseResponse<HoldPointsResponse> holdPointsResponseBaseResponse) {
                        callback.onSuccess(holdPointsResponseBaseResponse.getResponse());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void redeemPoints(RedeemPointBody body, final Callback callback) {
        transactionRemoteDataSource.redeemPoints(body)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void reverseHeldPoints(ReverseHeldPointsbody body, final Callback callback) {
        transactionRemoteDataSource.reverseHeldPoints(body)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void getPlayerBalance(GetPlayerBalanceBody body, final Callback<PlayerBalanceResponse> callback) {
        transactionRemoteDataSource.getPlayerBalance(body)
                .subscribe(new SingleObserver<BaseResponse<PlayerBalanceResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BaseResponse<PlayerBalanceResponse> playerBalanceResponseBaseResponse) {
                        callback.onSuccess(playerBalanceResponseBaseResponse.getResponse());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    private void addReferral(ReferralBody body, final Callback callback) {

        Log.i("referral_body", new Gson().toJson(body));
        transactionRemoteDataSource.addReferral(body)
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(new Object());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
    }

    public void addReferral(@NonNull Activity activity, @NonNull Intent intent, @NonNull final Callback callback) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(activity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String referralCode = deepLink.getQueryParameter("GBReferral");

                            ReferralBody referralBody = new ReferralBody();
                            referralBody.setNewPlayerUniqueId(SharedPreferencesUtils.getInstance().getPlayerUniqueId());
                            referralBody.setPlayerCode(referralCode);
                            referralBody.setNewPlayerTypeIDd(-1);
                            addReferral(referralBody, callback);
                        }

                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(this.getClass().getSimpleName(), "getDynamicLink:onFailure", e);
                        callback.onError(e);
                    }
                });
    }


    public void showNotification() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.gb_custom_toast_layout, null);

        Toast toast = new Toast(mContext);

        //use both property in single function
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
