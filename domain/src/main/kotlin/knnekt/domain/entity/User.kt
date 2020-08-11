package knnekt.domain.entity

data class User(
    val fullName: String?,
    val login: String,
    val phone: String,
    val avatar: String?
)

/*
  protected String fullName;
    protected String email;
    protected String login;
    protected String phone;
    protected String website;
    @SerializedName("last_request_at")
    protected Date lastRequestAt;
    @SerializedName("external_user_id")
    protected String externalId;
    @SerializedName("facebook_id")
    protected String facebookId;
    @SerializedName("twitter_id")
    protected String twitterId;
    @SerializedName("blob_id")
    @Deprecated
    protected Integer blobId;
    @SerializedName("user_tags")
    protected String tags;
    protected String password;
    protected String oldPassword;
    @SerializedName("custom_data")
    private String customData;
    @SerializedName("avatar")
    private String avatar;
 */