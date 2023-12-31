package com.gnusl.actine.network;

public enum Urls {

    Schema("http://"),
//    Host(Schema.getLink() + "atcine.com/"),
    Host(Schema.getLink() + "atcine.com/v3/"),
    API(Host.getLink() + "api/"),
    Auth(API.getLink() + "auth/"),
    Login(Auth.getLink() + "login"),
    Register(Auth.getLink() + "register"),
    Movies(API.getLink() + "movies/"),
    Movie(API.getLink() + "movie/"),
    MoviesGroups(Movies.getLink() + "groups"),
    MovieSuggest(Movie.getLink() + "%id%/suggest"),
    MovieFavorite(Movie.getLink() + "%id%/favourite"),
    MovieRemind(Movie.getLink() + "%id%/remind"),
    MovieDownload(Movie.getLink() + "%id%/download"),
    MoviesMyList(Movies.getLink() + "favourite"),
    MoviesSoon(Movies.getLink() + "soon"),
    MoviesDownloaded(Movies.getLink() + "download"),
    MovieComments(Movie.getLink() + "%id%/comment"),
    MovieCast(Movie.getLink() + "%id%/crew"),
    MovieLike(Movie.getLink() + "%id%/like"),
    MovieComment(Movie.getLink() + "comment/%id%/delete"),

    Series(API.getLink() + "series/"),
    Serie(API.getLink() + "serie/"),
    SeriesGroups(Series.getLink() + "groups"),
    SerieSuggest(Series.getLink() + "%id%/suggest"),
    SerieFavorite(Series.getLink() + "%id%/favourite"),
    SeriesMyList(Series.getLink() + "favourite"),
    SeriesDownloaded(Series.getLink() + "favourite"),
    SeriesComments(Series.getLink() + "%id%/comment"),
    SeriesLike(Series.getLink() + "%id%/like"),
    SeriesCommentDelete(Series.getLink() + "comment/%id%/delete"),

    Episode(API.getLink() + "episode/"),
    EpisodeComments(Episode.getLink() + "%id%/comment"),
    EpisodeLike(Episode.getLink() + "%id%/like"),
    EpisodeCast(Episode.getLink() + "%id%/crew"),
    EpisodeCommentDelete(Episode.getLink() + "comment/%id%/delete"),

    Categories(API.getLink() + "categories"),

    Pay(API.getLink() + "pay"),
    Account("http://atcine.com/user/account"),
    AccountUpdate(API.getLink() + "user/account"),

    Profiles(API.getLink() + "profiles"),
    Profile(API.getLink() + "profile"),
    UpdateProfile(Profile.getLink() + "/update"),
    CreateProfile(Profile.getLink() + "/create"),
    UserType(API.getLink() + "user/type"),

    Help(API.getLink() + "help"),
    LocationCheck(API.getLink() + "location/check"),
    Logout(API.getLink() + "user/logout"),

    SaveContinue(API.getLink() + "movie/continue"),
    GetContinue(API.getLink() + "movie/%id%/continue"),

    ;

    private String link;

    Urls(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
