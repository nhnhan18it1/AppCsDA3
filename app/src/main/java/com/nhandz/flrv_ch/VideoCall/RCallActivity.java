package com.nhandz.flrv_ch.VideoCall;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nhandz.flrv_ch.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.nhandz.flrv_ch.VideoCall.*;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;

public class RCallActivity extends AppCompatActivity implements SignallingClient.SignalingInterface{

    private Socket socket;
    private SeekBar seekBarX, seekBarY;

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

    SurfaceViewRenderer remoteVideoView;
    SurfaceViewRenderer selfVideoView;
    VideoCapturer videoCapturer;
    EglBase eglBase;
    boolean gotUserMedia;

    PeerConnection localPeer;
    Button btn_create, btn_TryStart, btn_Offer, btn_start;
    TextView txtTem,txthun;

    View mainView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sencall);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
//        try {
//            socket = IO.socket(ReadyActivity.serverNode);//http://40.74.112.141
//            socket.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        SignallingClient.getInstance().context=getApplicationContext();
        SignallingClient.getInstance().init(this);
        socket=SignallingClient.getInstance().socket;
        initView();

        start();
        //new TemAndHun().execute();



    }

    private void getIceServers() {
        byte[] data = new byte[0];
        try {
            data = ("nhavbnm:feffd4ec-afd5-11ea-b23e-0242ac150003").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //new Main2Activity.LoadIce().execute();
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
                        PeerConnection.IceServer iceServer1=PeerConnection.IceServer.builder(iceServer.url).createIceServer();
                        peericeServers.add(iceServer1);

                    }
                    else {
                        PeerConnection.IceServer iceServer1=PeerConnection.IceServer.builder(iceServer.url)
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

    private void start(){



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


        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).setEnableInternalTracer(true).createInitializationOptions());
        //peerConnectionFactory=PeerConnectionFactory.builder().createPeerConnectionFactory();


        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();

        videoCapturer=createVideoCapturer();

        MediaConstraints mediaConstraints=new MediaConstraints();
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.getEglBaseContext());
        videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
        localvideoTrack=peerConnectionFactory.createVideoTrack("100",videoSource);

        audioSource=peerConnectionFactory.createAudioSource(mediaConstraints);
        localAudioTrack=peerConnectionFactory.createAudioTrack("101",audioSource);

        remoteVideoView.setEnabled(true);


        gotUserMedia=true;
        if (SignallingClient.getInstance().isInitiator){
            onTryToStart();
        }
        selfVideoView.setEnabled(true);
        remoteVideoView.setEnabled(true);
        if (videoCapturer != null) {
            videoCapturer.startCapture(1024, 720, 30);
        }


        ProxyVideoSink localVideoSink = new ProxyVideoSink();

        localvideoTrack.addSink(selfVideoView);
        localVideoSink.setTarget(selfVideoView);


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
        //if ()
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                Log.e("onCreateSuccess", "SignallingClient emit ");
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }
        }, sdpConstraints);
    }

    public void onIceCandidateReceived(PeerConnection localPeer, IceCandidate iceCandidate) {
        //we have received ice candidate. We can set it to the other peer.
        SignallingClient.getInstance().emitIceCandidate(iceCandidate);
    }

    public void initView(){
        mainView = findViewById(R.id.main_view);
        remoteVideoView=findViewById(R.id.camera_render_car);
        btn_create=findViewById(R.id.btn_car_createR);
        btn_Offer=findViewById(R.id.btn_offer);
        btn_start=findViewById(R.id.btn_setStart);
        btn_TryStart=findViewById(R.id.btn_trytoS);
        selfVideoView=findViewById(R.id.camera_self);
        SetEvent();
    }
    public void SetEvent(){
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignallingClient.getInstance().emitInitStatement_create("123");
            }
        });

        btn_TryStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignallingClient.getInstance().emitMessage("get user media");
                SignallingClient.getInstance().isStarted=true;
                onTryToStart();
                //SignallingClient.getInstance().emitMessage("get user media");
            }
        });

        btn_Offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                createPeerConnection();
                SignallingClient.getInstance().emitMessage("get user media");
                doAnswer();
                call();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignallingClient.getInstance().emitInitStatement_join("123");
                call();
            }
        });


    }
    private void initVideos() {
        eglBase = EglBase.create();
        remoteVideoView.init(eglBase.getEglBaseContext(), null);
        remoteVideoView.setZOrderMediaOverlay(true);
        selfVideoView.init(eglBase.getEglBaseContext(),null);
        selfVideoView.setZOrderMediaOverlay(true);
    }

    private void hangup() {
        if (localPeer!=null){
            localPeer.close();
        }
        localPeer = null;
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

        localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new CustomPeerConnectionObserver("localPeerCreation"){
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                onIceCandidateReceived(localPeer,iceCandidate);
                Log.e("main2", "createPeerConnection: "+iceCandidate );
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Log.e("main2", "onAddStream: Received Remote stream" +mediaStream);
                super.onAddStream(mediaStream);
                gotRemoteStream(mediaStream);
            }
        });

        addStreamToLocalPeer();
    }

    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(final SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
                SignallingClient.getInstance().emitMessage(sessionDescription);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            SignallingClient.getInstance().emitMessage(sessionDescription);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, new MediaConstraints());

        Log.e("Car","doanswer");
    }

    @Override
    public void onRemoteHangUp(String msg) {

    }

    @Override
    public void onOfferReceived(JSONObject data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!SignallingClient.getInstance().isInitiator && !SignallingClient.getInstance().isStarted) {
                    RCallActivity.this.onTryToStart();
                }

                try {
                    RCallActivity.this.createPeerConnection();
                    localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
                    RCallActivity.this.doAnswer();
                    //updateVideoViews(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAnswerReceived(JSONObject data) {
        Log.e("CarAt", "onAnswerReceived: "+data);
        try {
            createPeerConnection();
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            //updateVideoViews(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIceCandidateReceived(JSONObject data) {
        Log.e("CarAt", "onIceCandidateReceived: " );
        try {
            if (localPeer==null)createPeerConnection();
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTryToStart() {
        Log.e("CarAt", "onTryToStart: localvideoTrack="+localvideoTrack+"--isInitiator="+SignallingClient.getInstance().isInitiator+" isStart="+SignallingClient.getInstance().isStarted+" isChanelReady="+ SignallingClient.getInstance().isChannelReady);
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
        Log.e("CarAt", "onCreatedRoom: "+gotUserMedia);
        if (gotUserMedia){
            SignallingClient.getInstance().emitMessage("get user media");
        }
    }

    @Override
    public void onJoinedRoom() {
        //Toast.makeText(this, "You join a romm "+gotUserMedia, Toast.LENGTH_SHORT).show();
        Log.e("CarAt", "onJoinedRoom: "+gotUserMedia );
        if (gotUserMedia){
            SignallingClient.getInstance().emitMessage("get user media");
        }
    }

    @Override
    public void onNewPeerJoined() {
//        Toast.makeText(this, "new peer join", Toast.LENGTH_SHORT).show();
        Log.e("CarAt", "onNewPeerJoined: " );
    }



    public class TemAndHun extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int i=0;
            while (true){
                i++;
                if (i==5){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("type","dht");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("control",jsonObject);
                    Log.e("car-", "doInBackground: "+jsonObject );
                    i=0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //return null;
        }
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
}