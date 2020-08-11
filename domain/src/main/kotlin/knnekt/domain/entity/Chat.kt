package knnekt.domain.entity

import java.util.*

data class Chat (
    val id: String,
    val lastMessage: String?,
    val lastMessageDateSent: Long,
    val lastMessageUserId: Int,
    val photo: String,
    val userId: Int,
    val roomJid: String?,
    val unreadMessageCount: Int?,
    val name: String,
    val type: Int,
    val description: String?,
    val occupantsCount: Int?,
    val occupants: List<Int>,
    val pinnedMessagesIds: List<String>,
    val adminsIds: List<Int>,
    val createdAt: Date,
    val updatedAt: Date
)


/*
 @SerializedName("_id")
    private String dialogId;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("last_message_date_sent")
    private long lastMessageDateSent;
    @SerializedName("last_message_user_id")
    private Integer lastMessageUserId;
    @SerializedName("photo")
    private String photo;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("xmpp_room_jid")
    private String roomJid;
    @SerializedName("unread_messages_count")
    private Integer unreadMessageCount;
    private String name;
    @SerializedName("occupants_ids")
    private List<Integer> occupantsIds;
    @SerializedName("pinned_messages_ids")
    private List<String> pinnedMessagesIds;
    private Integer type;
    @SerializedName("admins_ids")
    private List<Integer> adminsIds;
    @SerializedName("data")
    ConnectycubeDialogCustomData customData;
    @SerializedName("description")
    private String description;
    @SerializedName("occupants_count")
    private Integer occupantsCount;
 */