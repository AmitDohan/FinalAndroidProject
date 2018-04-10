package amitdohan.johnbryce.finalproject;


public class MovieSample {

    private String movieName, movieDes, moviePic;
    private int id, orderNumber, watched=0;

    public MovieSample(){}

    public MovieSample(int id, String movieName, String movieDes, String moviePic){
        this.id = id;
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.moviePic = moviePic;
    }
    public MovieSample(String movieName, String movieDes, String moviePic){
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.moviePic = moviePic;
    }
    public MovieSample(String movieName, String movieDes){
        this.movieName = movieName;
        this.movieDes = movieDes;

    }
    public MovieSample(String movieName){
        this.movieName = movieName;
    }
    public MovieSample(int id, String movieName, String movieDes, String moviePic, int orderNumber){
        this.id = id;
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.moviePic = moviePic;
        this.orderNumber = orderNumber;
    }
    public MovieSample(int id, String movieName, String movieDes, String moviePic, int orderNumber, int watched){
        this.id = id;
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.moviePic = moviePic;
        this.orderNumber = orderNumber;
        this.watched=watched;
    }
    public MovieSample(String movieName, String movieDes, String moviePic, int orderNumber){
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.moviePic = moviePic;
        this.orderNumber = orderNumber;
    }
    public MovieSample(String movieName, String movieDes, int orderNumber){
        this.movieName = movieName;
        this.movieDes = movieDes;
        this.orderNumber = orderNumber;
    }
    public MovieSample(String movieName, int orderNumber){

        this.movieName = movieName;
        this.orderNumber = orderNumber;

    }


    public int getWatched() {
        return watched;
    }
    public void setWatched(int watched) {
        this.watched = watched;
    }
    public int getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public String getMovieDes() {
        return movieDes;
    }
    public void setMovieDes(String movieDes) {
        this.movieDes = movieDes;
    }
    public String getMoviePic() {
        return moviePic;
    }
    public void setMoviePic(String url) {
        this.moviePic = url;
    }

}
