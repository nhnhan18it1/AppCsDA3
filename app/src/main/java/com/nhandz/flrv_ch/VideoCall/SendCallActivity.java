package com.nhandz.flrv_ch.VideoCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendCallActivity extends AppCompatActivity implements SignallingClient.SignalingInterface {

    private static final int ALL_PERMISSIONS_CODE = 1 ;
    private String TAG=getClass().getSimpleName();

    List<PeerConnection.IceServer> peericeServers=new ArrayList<>();
    List<IceServer> iceServers;
    PeerConnectionFactory peerConnectionFactory;
    MediaConstraints audioConstraints;
    MediaConstraints videoConstraints;
    MediaConstraints sdpConstraints;
    VideoSource videoSource;
    VideoTrack localvideoTrack;
    AudioSource audioSource;
    AudioTrack localAudioTrack;

    SurfaceViewRenderer localVideoView;
    SurfaceViewRenderer remoteVideoView;
    VideoCapturer videoCapturer;
    EglBase eglBase;
    boolean gotUserMedia;

    PeerConnection localPeer, remotePeer;
    Button create, join, start, trystart;
    EditText txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
        setContentView(R.layout.activity_rcall);
        //Toast.makeText(this, "main2", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, ALL_PERMISSIONS_CODE);
        } else {
            // all permissions already granted
            start();
        }
        create=findViewById(R.id.button_create);
        join=findViewById(R.id.button_join);
        start=findViewById(R.id.button_start);
        //txtname=findViewById(R.id.txt_name);
        trystart=findViewById(R.id.trytostart);
        SignallingClient.getInstance().context=getApplicationContext();
        SignallingClient.getInstance().init(this);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignallingClient.getInstance().emitInitStatement_create("123");
                //doAnswer();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignallingClient.getInstance().emitInitStatement_join("123");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignallingClient.getInstance().emitMessage("get user media");
                call();
            }
        });
        trystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignallingClient.getInstance().isStarted=false;
                onTryToStart();
            }
        });
        Intent intent=getIntent();
        if (intent.getStringExtra("Type").equals("R")){
            try {

                JSONObject jsonObject=new JSONObject(intent.getStringExtra("data"));
                //SignallingClient.getInstance().emitInitStatement_join(jsonObject.getString("IDS")+MainActivity.OnAccount.getID());
                SignallingClient.getInstance().isStarted=false;
                onTryToStart();
                Log.e(TAG, "onCreate: onJoin-R" );

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if (intent.getStringExtra("Type").equals("S")){
            SignallingClient.getInstance().emitInitStatement_create(MainActivity.OnAccount.getID()+intent.getStringExtra("ID"));
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("IDNG",String.valueOf(MainActivity.OnAccount.getID()));
                jsonObject.put("Avt",MainActivity.OnAccount.getAvt());
                jsonObject.put("name",MainActivity.OnAccount.getName());
                jsonObject.put("IDNN",intent.getStringExtra("ID"));
                Log.e(TAG, "onCreate: "+jsonObject.toString() );
                MainActivity.mSocket.emit("request_to",jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            MainActivity.mSocket.on("join", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "call: joined" );
                    SignallingClient.getInstance().isStarted=false;
                    onTryToStart();
                    //scall();
                    //call();
                }
            });
        }

        //call();
    }

    public void scall(){
        call();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS_CODE
                && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // all permissions granted
            start();
        } else {
            //finish();
        }
    }

    private void getIceServers() {
        byte[] data = new byte[0];
        try {
            data = ("nhavbnm:feffd4ec-afd5-11ea-b23e-0242ac150003").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new LoadIce().execute();
        String authToken = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);//
        Utils.getInstance().getRetrofitInstance().getIceCandidates().enqueue(new Callback<TurnServerPojo>() {
            @Override
            public void onResponse(Call<TurnServerPojo> call, Response<TurnServerPojo> response) {
                TurnServerPojo body=response.body();
                Log.e("main2","getIce"+body);
                if (body!=null){
                    iceServers=body.iceServerList.iceServers;
                }
                for (IceServer iceServer: iceServers){
                    if (iceServer.credential==null){
                        PeerConnection.IceServer iceServer1= PeerConnection.IceServer.builder(iceServer.url).createIceServer();
                        peericeServers.add(iceServer1);

                    }
                    else {
                        PeerConnection.IceServer iceServer1= PeerConnection.IceServer.builder(iceServer.url)
                                .setUsername(iceServer.username)
                                .setPassword(iceServer.credential)
                                .createIceServer();
                        peericeServers.add(iceServer1);
                    }
                    Log.e("onApiResponse", "IceServers--"+peericeServers.size()+"--" + iceServers.toString());
                }
            }



            @Override
            public void onFailure(Call<TurnServerPojo> call, Throwable t) {
            t.printStackTrace();
                Log.e("onApiResponse-false", "IceServers"+t.getMessage() );
            }
        });
    }

    private void initVideos() {
        eglBase = EglBase.create();
        localVideoView=findViewById(R.id.surface_rendeer);
        remoteVideoView = findViewById(R.id.Remote_surface_rendeer);
        localVideoView.init(eglBase.getEglBaseContext(), null);
        localVideoView.setEnableHardwareScaler(true);
        remoteVideoView.init(eglBase.getEglBaseContext(), null);
        localVideoView.setMirror(true);
        localVideoView.setZOrderMediaOverlay(true);
//        remoteVideoView.setZOrderMediaOverlay(true);
    }

    private void start(){

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getIceServers();
//        VideoCapturer ss =createCameraCapturer(new Camera1Enumerator(false));
//        if (ss!=null){
//            videoCapturer=ss;
//            Log.e("TAG", "VideoCapturer: != null" );
//        }
//        else {
//            Log.e("TAG", "VideoCapturer: == null" );
//
//        }

        initVideos();

        videoCapturer=createCameraCapturer(new Camera1Enumerator(false));;
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).setEnableInternalTracer(true).createInitializationOptions());
        //peerConnectionFactory=PeerConnectionFactory.builder().createPeerConnectionFactory();



        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();


        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());
        peerConnectionFactory = PeerConnectionFactory.builder()

                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();



        MediaConstraints mediaConstraints=new MediaConstraints();
        if (videoCapturer != null) {
            SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.getEglBaseContext());
            videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
            videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
        }

        localvideoTrack=peerConnectionFactory.createVideoTrack("100",videoSource);

        audioSource=peerConnectionFactory.createAudioSource(mediaConstraints);
        localAudioTrack=peerConnectionFactory.createAudioTrack("101",audioSource);


        localVideoView.setEnabled(true);
        remoteVideoView.setEnabled(true);
        if (videoCapturer != null) {
            videoCapturer.startCapture(1024, 720, 30);
        }


        ProxyVideoSink localVideoSink = new ProxyVideoSink();

        localvideoTrack.addSink(localVideoView);
        localVideoSink.setTarget(localVideoView);


        gotUserMedia=true;
        if (SignallingClient.getInstance().isInitiator){
            onTryToStart();
        }
        //onTryToStart();

    }


    private static class ProxyVideoSink implements VideoSink {
        private VideoSink target;

        @Override
        synchronized public void onFrame(VideoFrame frame) {
            if (target == null) {
                Log.e("TAG", "Dropping frame in proxy because target is null.");
                return;
            }

            target.onFrame(frame);
        }

        synchronized public void setTarget(VideoSink target) {
            this.target = target;
        }
    }



    public void call(){
        sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                Log.e("onCreateSuccess", "SignallingClient emit ");
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }

            @Override
            public void onCreateFailure(String s){
                super.onCreateFailure(s);
                Log.e("main 2", "onCreateFailure: "+s );
            }

        }, sdpConstraints);

    }


    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        Log.d("TAG", "Creating capturer using camera1 API.");
        videoCapturer = createCameraCapturer(new Camera2Enumerator(getApplicationContext()));

        return videoCapturer;
    }


    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d("TAG", "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d("TAG", "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }


        // Front facing camera not found, try something else
        Log.d("TAG", "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isBackFacing(deviceName)) {
                Log.d("TAG", "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }
    public void onIceCandidateReceived(PeerConnection localPeer, IceCandidate iceCandidate) {
        //we have received ice candidate. We can set it to the other peer.
        SignallingClient.getInstance().emitIceCandidate(iceCandidate);
    }

    @Override
    public void onBackPressed() {
        hangup();
        super.onBackPressed();
    }

    private void hangup() {
        if (localPeer!=null){
            localPeer.close();
        }
        if (remotePeer!=null)remotePeer.close();

        localPeer = null;
        remotePeer = null;
        //start.setEnabled(true);
        //call.setEnabled(false);
        //hangup.setEnabled(false);
    }

    private void gotRemoteStream(MediaStream stream) {
        //we have remote video stream. add to the renderer.
        final VideoTrack videoTrack = stream.videoTracks.get(0);
        AudioTrack audioTrack = stream.audioTracks.get(0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //remoteRenderer = new VideoRenderer(remoteVideoView);
                    remoteVideoView.setVisibility(View.VISIBLE);
                    videoTrack.addSink(remoteVideoView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void addStreamToLocalPeer() {
        //creating local mediastream
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("102");
        stream.addTrack(localAudioTrack);
        stream.addTrack(localvideoTrack);
        localPeer.addStream(stream);
    }

    private void updateVideoViews(final boolean remoteVisible) {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = localVideoView.getLayoutParams();
            if (remoteVisible) {
                params.height = 250;
                params.width = 250;
            } else {
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            localVideoView.setLayoutParams(params);
        });
    }

    private void createPeerConnection() {

        PeerConnection.RTCConfiguration rtcConfig =
                new PeerConnection.RTCConfiguration(peericeServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
//        localPeer= peerConnectionFactory.createPeerConnection(peericeServers, new PeerConnection.Observer() {
//            @Override
//            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//
//            }
//
//            @Override
//            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//
//            }
//
//            @Override
//            public void onIceConnectionReceivingChange(boolean b) {
//
//            }
//
//            @Override
//            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//
//            }
//
//            @Override
//            public void onIceCandidate(IceCandidate iceCandidate) {
//                onIceCandidateReceived(localPeer,iceCandidate);
//                Log.e("main2", "createPeerConnection: "+iceCandidate );
//            }
//
//            @Override
//            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//
//            }
//
//            @Override
//            public void onAddStream(MediaStream mediaStream) {
//                Log.e("main2", "onAddStream: Received Remote stream"+mediaStream );
//                gotRemoteStream(mediaStream);
//            }
//
//            @Override
//            public void onRemoveStream(MediaStream mediaStream) {
//
//            }
//
//            @Override
//            public void onDataChannel(DataChannel dataChannel) {
//
//            }
//
//            @Override
//            public void onRenegotiationNeeded() {
//
//            }
//
//            @Override
//            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
//                final VideoTrack videoTrack = mediaStreams[0].videoTracks.get(0);
//                AudioTrack audioTrack = mediaStreams[0].audioTracks.get(0);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            //remoteRenderer = new VideoRenderer(remoteVideoView);
//                            remoteVideoView.setVisibility(View.VISIBLE);
//                            videoTrack.addSink(remoteVideoView);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
        localPeer = peerConnectionFactory.createPeerConnection(peericeServers, new CustomPeerConnectionObserver("localPeerCreation"){
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                onIceCandidateReceived(localPeer,iceCandidate);
                Log.e("main2", "createPeerConnection: "+iceCandidate );
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Log.e("main2", "onAddStream: Received Remote stream" );
                super.onAddStream(mediaStream);
                gotRemoteStream(mediaStream);
            }
        });

        addStreamToLocalPeer();


    }


    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }

            @Override
            public void onCreateFailure(String s) {
                super.onCreateFailure(s);
                Log.e("main2", "onCreateFailure: "+s );
            }
        }, new MediaConstraints());

        Log.e("Car","doanswer");
    }
    @Override
    public void onRemoteHangUp(String msg) {
        runOnUiThread(this::hangup);
    }

    @Override
    public void onOfferReceived(JSONObject data) {
        runOnUiThread(() -> {
            if (!SignallingClient.getInstance().isInitiator && !SignallingClient.getInstance().isStarted) {
                onTryToStart();
            }

            try {
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
                doAnswer();
                //updateVideoViews(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }



    @Override
    public void onAnswerReceived(JSONObject data) {
        Log.e("main2", "onAnswerReceived: "+data);
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            updateVideoViews(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIceCandidateReceived(JSONObject data) {
        Log.e("main2", "onIceCandidateReceived: "+data );
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTryToStart() {
        Log.e("main2", "onTryToStart: localvideoTrack="+localvideoTrack+"--isInitiator="+SignallingClient.getInstance().isInitiator+" isStart="+SignallingClient.getInstance().isStarted+" isChanelReady="+ SignallingClient.getInstance().isChannelReady);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!SignallingClient.getInstance().isStarted
                        &&localvideoTrack!=null
                        &&SignallingClient.getInstance().isChannelReady
                ){
                    createPeerConnection();
                    SignallingClient.getInstance().isStarted=true;
                    if (SignallingClient.getInstance().isInitiator){
                        call();
                    }
                }
            }
        });

    }

    @Override
    public void onCreatedRoom() {
        //Toast.makeText(getApplicationContext(), "You create a romm "+gotUserMedia, Toast.LENGTH_SHORT).show();
        Log.e("main2", "onCreatedRoom: "+gotUserMedia);
        if (gotUserMedia){
            SignallingClient.getInstance().emitMessage("get user media");
        }
    }

    @Override
    public void onJoinedRoom() {
        //Toast.makeText(this, "You join a romm "+gotUserMedia, Toast.LENGTH_SHORT).show();
        Log.e("main2", "onJoinedRoom: "+gotUserMedia );
        if (gotUserMedia){
            SignallingClient.getInstance().emitMessage("get user media");
        }
    }

    @Override
    public void onNewPeerJoined() {
//        Toast.makeText(this, "new peer join", Toast.LENGTH_SHORT).show();
        Log.e("mai2", "onNewPeerJoined: " );
    }

    public class LoadIce extends AsyncTask<Void,Void,String>{

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("x","x")
                    .build();
            Request request=new Request.Builder()
                    .url(MainActivity.Nodeserver)
                    .method("post",requestBody)
                    .build();
            try {
                okhttp3.Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("main2-loadice", "onPostExecute: "+s );
        }
    }
}
