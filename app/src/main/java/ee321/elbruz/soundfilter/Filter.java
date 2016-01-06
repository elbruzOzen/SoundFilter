package ee321.elbruz.soundfilter;

public class Filter {

    public Filter(){
    }

    public short[] lpf(short[] x, int fc,int fs, int degree) {

        int size = x.length;
        short[] y = new short[size];
        double nwc = (((double)fc/(double)fs) * 2 * Math.PI);

        for(int i = 0 ; i < degree ; i++)
            y[i] = x[i];

        for(int i = degree; i < size-degree ; i++){

            double temp = 0;

            for(int j = -degree ; j < degree+1 ; j++){
                temp = temp + mySinc(nwc,j) * (double)x[i-j];
            }

            y[i] = (short)(temp);
            //System.out.println("" + y[i]);

        }

        for(int i = degree ; i > 0 ; i--)
            y[size-i] = x[size-i];

        return y;
    }

    public short[] hpf(short[] x, int fc,int fs, int degree) {

        int size = x.length;
        short[] y = new short[size];
        double nwc = (((double)fc/(double)fs) * 2 * Math.PI);

        for(int i = 0 ; i < degree ; i++)
            y[i] = x[i];

        for(int i = degree; i < size-degree ; i++){

            double temp = 0;

            for(int j = -degree ; j < degree+1 ; j++){
                temp = temp +(impulse(j)-mySinc(nwc,j)) * (double)x[i-j];
            }

            y[i] = (short)(temp);
            //System.out.println("" + y[i]);

        }

        for(int i = degree ; i > 0 ; i--)
            y[size-i] = x[size-i];

        return y;
    }

    public short[] bpf(short[] x, int fc,int f0, int fs, int degree) {

        int size = x.length;
        short[] y = new short[size];
        double nwc = (((double)fc/(double)fs) * 2 * Math.PI);
        double nw0 = (((double)f0/(double)fs) * 2 * Math.PI);

        for(int i = 0 ; i < degree ; i++)
            y[i] = x[i];

        for(int i = degree; i < size-degree ; i++){

            double temp = 0;

            for(int j = -degree ; j < degree+1 ; j++){
                temp = temp +(2*Math.cos(nw0*j)*mySinc(nwc,j)) * (double)x[i-j];
            }

            y[i] = (short)(temp);
            //System.out.println("" + y[i]);

        }

        for(int i = degree ; i > 0 ; i--)
            y[size-i] = x[size-i];

        return y;
    }

    public short[] bsf(short[] x, int fc,int f0, int fs, int degree) {

        int size = x.length;
        short[] y = new short[size];
        double nwc = (((double)fc/(double)fs) * 2 * Math.PI);
        double nw0 = (((double)f0/(double)fs) * 2 * Math.PI);

        for(int i = 0 ; i < degree ; i++)
            y[i] = x[i];

        for(int i = degree; i < size-degree ; i++){

            double temp = 0;

            for(int j = -degree ; j < degree+1 ; j++){
                temp = temp +(impulse(j)-2*Math.cos(nw0*j)*mySinc(nwc,j)) * (double)x[i-j];
            }

            y[i] = (short)(temp);
            //System.out.println("" + y[i]);

        }

        for(int i = degree ; i > 0 ; i--)
            y[size-i] = x[size-i];

        return y;
    }


    //Return sinc function
    private double mySinc(double nwc,int n){
        if(n != 0){
            return (Math.sin(nwc*n))/((double)n*Math.PI);
        }else{
            return nwc / Math.PI;
        }
    }

    //Return impulse function
    private double impulse(int n){
        if(n == 0)
            return 1;
        else
            return 0;
    }

}