import java.util.*;
class crejo{

    public static void main(String[] args){
          
    
        reviewService Review=new reviewService();
        
        Review.addMovies("Don released in Year 2006 for Genres Action&Comedy");
         Review.addMovies("Tiger released in Year 2008 for Genre Drama");
         Review.addMovies("Padmaavat released in Year 2006 for Genre Comedy");
         Review.addMovies("Lunchbox released in Year 2021 for Genre Drama");
         Review.addMovies("Guru released in Year 2006 for Genre Drama");
         Review.addMovies("Metro released in Year 2006 for Genre Romance");
         Review.addUsers("SRK");
         Review.addUsers("Salman");
         Review.addUsers("Deepika");
         Review.addReview("SRK","Don",2);
         Review.addReview("SRK","Padmaavat",8);
         Review.addReview("Salman","Don",5);
         Review.addReview("Deepika","Don",9);
         Review.addReview("Deepika","Guru",6);
         Review.addReview("SRK","Don",10);// exception
         Review.addReview("Deepika","Lunchbox",5);
         Review.addReview("SRK","Tiger",5);
         Review.addReview("SRK","Metro",7);
      

    }

}

class reviewService{

  //     3. List top n movies by total review score by ‘ critics’ i n a particular genre.
  // 4. Average review score i n a particular year of release.
  // 5. Average review score for a particular movie.
    private HashMap<String,Movie> movieMap;
    private HashMap<String,User> userMap;
    private HashMap<Integer,String> yearlyRatings;
    private ArrayList<String> mNameByCritic; 
    reviewService(){
      
       this.yearlyRatings=new HashMap<>();
      //  this.critics=new HashSet<>();
        this.userMap=new HashMap<>();
        this.movieMap=new HashMap<>();
        this.mNameByCritic=new ArrayList<>();
    }
  
  public class Movie{
  
       int rating=0;
       int usersReviewed=0;
       String mName="";
       String genre="";
       int releaseYear;
       Movie(String mName,String genre,int releaseYear ){
            this.mName=mName;
            this.genre=genre;
            this.releaseYear=releaseYear;
       }
  }
  public class User{
      boolean iscritic=false;
      String  uName="";
      // int revPublished=0;/
      HashMap<String,Integer> movieReviews ;
       User(String uname){
           movieReviews=new HashMap<>();
            this.uName=uname;
       }
  }
  
 public void addReview(String userName,String movie,int ratings){
           try{
            addReviewP(userName,movie,ratings);
           }catch(Exception e){
              System.out.println(e.toString());
           }
           
 }
  private void addReviewP(String userName,String movie,int ratings) throws IllegalAccessException{
     // check ki user already rating de chuka hai ya nhi same movie ko
       User user=userMap.get(userName);  
     if(user.movieReviews.containsKey(movie)){
         throw new IllegalAccessException("Multiple Reviews Not allowed"); // we have to throw exxception here 
        // return ;
     }
   
  
      
      // movie present hai ya nhi
      if(!movieMap.containsKey(movie)){
         throw new IllegalAccessException("Movie Not Released Yet");
          // return ;
      }
      
       
      // check karo critic hai ya nhi .. agar hai pada to reviews 2rating s se + karo
      Movie movieo=movieMap.get(movie);
      if(user.iscritic){
                 // store 2into ratings 
         user.movieReviews.put(movie,2*ratings);
         movieo.rating +=(2*ratings);
         
         mNameByCritic.add(movie);  // adding movies rated by critics
       }else{
            //agar critic nhi hai to  review increase karo and check kro critic bana hai ya nhi 
              // use ratings normally
        user.movieReviews.put(movie,ratings);
        movieo.rating+=ratings;
  
        if(user.movieReviews.size()>=3){
            user.iscritic=true;
            System.out.println(userName+" is promoted to critic now");
        }
       }
       movieo.usersReviewed+=1;
       // movie ke object me ratings increase krni hai 
  }
  public void addUsers(String name) {
     try{ 
    addUsersp(name);
     }catch(IllegalAccessException e){
        System.out.println(e.toString());
     }
  }
  private void addUsersp(String uname) throws IllegalAccessException{ 
   
           if(userMap.containsKey(uname)){
             throw new IllegalAccessException("User Already Present");
            //  return ;
           }
     User user=new User(uname); 
       userMap.put(uname,user);
  
          System.out.println("User"+ uname +"Added Successfully");
      }
  
  public void addMovies(String movieDetails) {
      
    String[] details=movieDetails.split(" ");
     String name=details[0];
     int year=Integer.parseInt(details[4]);
     String genre=details[details.length-1];
  
      try{
      addMovies(name,year,genre);
      }catch(IllegalAccessException e){
        System.out.println(e.toString());
      }
  }
  
  private void addMovies(String name,int year,String genre) throws IllegalAccessException{
      
     if(movieMap.containsKey(name)){
  
      throw new IllegalAccessException("Movie Already Released");
      // return ;
     }
  
      Movie newMovie=new Movie(name,genre,year);
      movieMap.put(name,newMovie);
        // year vise movies ratings 
      // ArrayList<Movie> list=yearlyMovies.getOrDefault(year,new ArrayList<>());
      // list.add(newMovie);
      // yearlyRatings.put(year,list); 
  
      System.out.println("Movie "+ name +"Released in "+year+"with genre "+genre );
  }
  
   public void topMovies(int n,String genre){
  //  List top n movies by total review score by ‘ critics’ in a particular genre.
           // can make loop of all the hashmap and find all the top n movies 
       
           PriorityQueue<Movie> pq=new PriorityQueue<>(new Comparator<Movie>(){
  
               public int compare(Movie a,Movie b){
  
                   return b.rating-a.rating;
               }
  
           });
            
           for(String mname:mNameByCritic){
                 Movie m= movieMap.get(mname);
              if(m.genre.equals(genre)){
                  pq.add(m);
              }
           } 
           List<String> list=new ArrayList<>();
           while(n-->0 && pq.size()>0){
                 list.add(pq.poll().mName);
           }
  
            if(list.size()==0){
              System.out.println("No movie of this genre "+ genre +" is present");
              return ;
            }
           System.out.println(list);
   }
    public void reviewByYear(int year){ // total reviews in this year
         int nousers=0;
         int totalRatings=0;
        for(Movie obj:movieMap.values()){
                  if(obj.releaseYear==year){
                      totalRatings+=obj.rating;
                      nousers++;
                  }    
                              
        }
  
        System.out.println(totalRatings/nousers);
    }
    public void reviewByNameAndYear(String movieName,int year){
        // reviews of particular movie in particular year 
         
        int nousers=0;
        int totalRatings=0;
       for(Movie obj:movieMap.values()){
                 if(obj.releaseYear==year && obj.mName.equals(movieName)){
                     totalRatings+=obj.rating;
                     nousers++;
                 }    
                             
       }
  
       System.out.println(totalRatings/nousers);
    }
    public void reviewScore(String movieName){
              // review of particular moview Name uptil now 
        
            Movie movie=movieMap.get(movieName);
  
              int nouser=movie.usersReviewed;
              int totalReviews=movie.rating;
  
  
       System.out.println(totalReviews/nouser);
    }
    
  }