package org.example;

public class Rules {
    // TODO update dit zodat het werkt voor meerdere containervelden
    Square square;
    public Rules() {
        int xlo=Integer.MAX_VALUE , ylo =Integer.MAX_VALUE, xrb=Integer.MIN_VALUE, yrb =Integer.MIN_VALUE;
        for (Storage storage : Data.storage){
            if (storage.x < xlo) xlo = storage.x;
            if (storage.y < ylo) ylo = storage.y;
            if (storage.x + Constants.storageWidth> xrb) xrb = storage.x;
            if (storage.y + Constants.storageHeight> yrb) yrb = storage.y;
            this.square = new Square(xlo, ylo, xrb, yrb);
        }
    }
    private class Square{
        public int xlo , ylo , xrb , yrb;
        public Square(int xlo, int ylo , int xrb ,int yrb ){
            this.xlo = xlo;
            this.ylo = ylo;
            this.xrb = xrb;
            this.yrb = yrb;

        }
    }
    public boolean canRotate(int x , int y , Direction direction){
        int xn ,yn;
        if (direction == Direction.down){
             xn = x-2;
             yn = y;
        }else{
             xn = x;
             yn = y-2;
        }
        if (xn < 0 || yn < 0 || xn +8 > Data.mapWidth || yn +8 > Data.mapHeight){return false;}
        // write a simple if statement to look if [xn ,xn+8] and [yn, yn+8] fall within the boundry of square
        // when it falls outside square return true
        return (xn + 8) < square.xlo ||   // entirely left of square
                (xn)      > square.xrb ||   // entirely right of square
                (yn + 8) < square.ylo ||   // entirely above square
                (yn)      > square.yrb;     // entirely below square

    }
}
