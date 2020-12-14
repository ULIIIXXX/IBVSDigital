import com.digitalpersona.uareu.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Comparation {


    public enum Evento{
        DETECTAR,
        FINALIZADO,
        ERROR
    }

    public Reader reader;
    Engine engine;
    byte[] data;

    /*Interfaz que recibe eventos de enrolamiento*/

    public  interface EnrolamientoListener{
        //El sensonser es activado cuando se genera esta interfaz
        void eventoEnrolamiento(Evento e,String msg);
    }

    public void cancelarDeteccion(){
        try{
            reader.CancelCapture();
            reader.Close();
        }catch(Exception ex){

        }
    }

    //Inicializa el lector y el motor de identificacion
    public Comparation(){
        try{
            ReaderCollection lectores = UareUGlobal.GetReaderCollection();
            int i  = 0;
            while(lectores.size() == 0 && i++<10){
                lectores.GetReaders();
            }
            reader = lectores.get(0);
            engine = UareUGlobal.GetEngine();
        }catch(UareUException | ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    //Enrola huella y envia eventos DETECTAR, FINALIZADO Y ERROR mediante el listener

    public void enrolar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    reader.Open(Reader.Priority.EXCLUSIVE);
                    final  Fmd huellas[] = new Fmd[1];
                    Fmd externa = null;
                    for(int i=0 ; i<huellas.length; i++){
                        System.out.println("DETECTAR");
                        huellas[i] = engine.CreateFmd(reader.Capture(Fid.Format.ANSI_381_2004,Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1).image, Fmd.Format.ANSI_378_2004);
                        //externa = engine.
                    }

                    Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004,new Engine.EnrollmentCallback(){
                        int i=0;
                        @Override
                        public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
                            Engine.PreEnrollmentFmd result = new Engine.PreEnrollmentFmd();
                            try{
                                result.fmd = huellas[i];
                            }catch (Exception ex){
                                ex.printStackTrace();
                                return  null;
                            }
                            return  result;
                        }
                    });
                    data = fmd.getData();
                    /*System.out.println("FINALIZADO");
                    System.out.println("Width " + fmd.getWidth());
                    System.out.println("Height " + fmd.getHeight() );
                    System.out.println("Resolution " + fmd.getResolution());
                    System.out.println("CbeffId " + fmd.getCbeffId());
                    System.out.println("format " + fmd.getFormat());
                    System.out.println("View Count  " + fmd.getViewCnt() + " " + fmd.getViews());
                    System.out.println(data);*/

                    //creacion
                    byte [] datas = null;
                    Fmd archivo = null;
                    BufferedImage imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\dedo.png"));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(imagen, "png", bos);
                    datas = bos.toByteArray();

                    //Comparacion
                   // archivo = engine.CreateFmd(datas,357,392,197,1,3407615,Fmd.Format.ANSI_378_2004);
                    //archivo = UareUGlobal.GetImporter().ImportFmd(datas,Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
                    archivo = engine.CreateFmd(toBytes(imagen),imagen.getWidth(),imagen.getHeight(),500,0,3407615,Fmd.Format.ISO_19794_2_2005);
                    if(engine.Compare(archivo,0,fmd,0) < 2000){
                        System.out.println("Persona autenticada, las huellas coinciden");
                        System.out.println("Resultado obtenido: " + engine.Compare(archivo,0,fmd,0));
                    }else{
                        System.out.println("El resultado no coincide");
                        System.out.println("Resultado obtenido: " + engine.Compare(archivo,0,fmd,0));
                    }
                    //System.out.println(engine.Compare(archivo,0,fmd,0));
                    reader.Close();
                }catch(UareUException ex){
                    cancelarDeteccion();
                    String r = ex.toString();
                    if(ex.toString().contains("FMD")){
                        System.out.println("LAS HUELLAS NO COINCIDEN");
                    }else{
                        System.out.println("LECTOR NO DISPONIBLE");
                    }
                    ex.printStackTrace();
                }catch (Exception ex){
                    cancelarDeteccion();
                    System.out.println("Error detectando, intenta de nuevo");
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public Fmd capturar(){
        Fmd fmd = null;
        try{
            reader.Close();
        }catch(UareUException ex){
            ex.printStackTrace();
        }
        try{
            reader.Open(Reader.Priority.EXCLUSIVE);
            Fid imagen = null;
            while(imagen == null){
                imagen = reader.Capture(Fid.Format.ANSI_381_2004,Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1).image;

                System.out.println("imagen = " + imagen);
            }
            fmd = engine.CreateFmd(imagen,Fmd.Format.ANSI_378_2004);
            reader.Close();
        }catch(UareUException ex){
            ex.printStackTrace();
        }
        return  fmd;
    }

    public byte[] ctoBA(){
        byte [] datas = null;
        try {
            BufferedImage imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\dedo.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", bos);
            datas = bos.toByteArray();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return  datas;
    }

    public Fmd fromImage(){
        byte [] datas = null;
        Fmd archivo = null;
        try {
            BufferedImage imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\papa.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", bos);
            bos.flush();
            datas = bos.toByteArray();

            // archivo = engine.CreateFmd(datas,357,392,197,0,3407615,Fmd.Format.ISO_19794_2_2005);
            archivo = engine.CreateFmd(toBytes(imagen),imagen.getWidth(),imagen.getHeight(),500,0,3407615,Fmd.Format.ISO_19794_2_2005);
            System.out.println(archivo.getData());

        }catch(IOException | UareUException ex){
            ex.printStackTrace();
        }

        return archivo;
    }

    //metodo que works
    public byte[] toBytes(BufferedImage image){
        WritableRaster raster = image.getRaster();
        DataBufferByte data= (DataBufferByte) raster.getDataBuffer();
        return (data.getData());
    }

    public static void main(String[]  args){
        System.out.println("Inicia el test");
        try{
            Comparation comparation = new Comparation();
            //comparation.fromImage();
            comparation.enrolar();
        }catch (ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

}
