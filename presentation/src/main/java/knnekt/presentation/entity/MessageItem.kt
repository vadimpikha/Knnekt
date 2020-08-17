package knnekt.presentation.entity

data class MessageItem (
    val id: String,
    val dateSent: Long,
    val body: String,
    val readIds: Collection<Int>,
    val deliveredIds: Collection<Int>,
    val viewsCount: Int,
    val recipientId: Int?,
    val senderId: Int?,
    val markable: Boolean,
    val delayed: Boolean
)

/*
private String _id;
    private String dialogId;
    private long dateSent = 0L;
    private String body;
    private Collection<Integer> readIds;
    private Collection<Integer> deliveredIds;
    private Integer viewsCount;
    private Integer recipientId;
    private Integer senderId;
    private boolean markable = false;
    private boolean delayed = false;
    private Map<String, String> properties;
    private Collection<ConnectycubeAttachment> attachments;
    private boolean saveToHistory = true;
    private ChatMessageExtension packetExtension;
    private HashMap<String, Object> complexProperties;
    private int destroyAfter;
 */