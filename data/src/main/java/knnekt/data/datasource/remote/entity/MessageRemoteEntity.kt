package knnekt.data.datasource.remote.entity

import knnekt.data.datasource.db.entity.AttachmentEntity

data class MessageRemoteEntity (
    val id: String,
    val chatId: String,
    val dateSend: Long,
    val body: String,
    val readIds: List<Int>,
    val deliveredIds: List<Int>,
    val senderId: Int,
    val attachments: List<AttachmentRemoteEntity>
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