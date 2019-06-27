package com.newfly.server;

import io.netty.channel.socket.SocketChannel;

// lobbyManager使用单例模式管理玩家和连接
public class LobbyManager {

    /**
     * 协议管理器
     */
    public static Map<Integer,Processors> protocolManager;

    /**
     * 客户端连接管理器
     */
    public static Map<String, SocketChannel> gateManager;

    /**
     * GameSession管理器
     */
    public static Map<String,LobbyGameSession> sessionManager;

    /**
     * 玩家管理器
     */
    public static Map<String,Player> playerManager;

    public void init(){
        protocolManager = LobbyProtocol.toMap();
        gateManager = new HashMap<String,SocketChannel>();
        sessionManager = new HashMap<String,LobbyGameSession>();
        playerManager = new HashMap<String,Player>();
        System.out.println("初始化管理器成功！");
    }

    private LobbyManager(){
    }

    private static LobbyManager lobbyManager = null;

    /**
     * 单例模式
     * @return
     */
    public static synchronized LobbyManager getInstance(){
        if(lobbyManager == null){
            lobbyManager = new LobbyManager();
        }
        return lobbyManager;
    }

    /**
     * 将新获取的连接增加到gateManager中
     * @param sessionId
     * @param socketChannel
     */
    public synchronized void addChannel(String sessionId,SocketChannel socketChannel){
        //直接put 如果已经存在相当于其他地方登录，直接覆盖
        if(socketChannel != null){
            gateManager.put(sessionId, socketChannel);
        }
    }

    /**
     * 根据sessionId获取SocketChannel
     * @param sessionId
     */
    public SocketChannel getChannel(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        return gateManager.get(sessionId);
    }

    /**
     * 新增gameSession
     * 如果已经存在了 很可能存在同一个ip用户又进行了其他账号的登录，这时直接覆盖
     * @param sessionId
     * @param gameSession
     */
    public synchronized void addSession(String sessionId,LobbyGameSession gameSession){
        if(gameSession != null){
            sessionManager.put(sessionId,gameSession);
        }
    }

    /**
     * 根据sessionId获取session
     * @param sessionId
     * @return
     */
    public LobbyGameSession getSession(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        return sessionManager.get(sessionId);
    }

    /**
     * 玩家离线,将玩家从gateManager中删掉
     * @param uuid
     */
    public synchronized void removeChannel(String sessionId){
        gateManager.remove(sessionId);
        //同时 删除掉玩家的session信息
        sessionManager.remove(sessionId);
    }

    /**
     * 新增用户到玩家管理器
     * @param uuid
     * @param player
     */
    public synchronized void addPlayer(String uuid,Player player){
        if(player != null){
            playerManager.put(uuid,player);
        }
    }

    /**
     * 根据用户Id从用户管理器中获取用户
     * @param uuid
     * @return
     */
    public Player getPlayer(String uuid){
        if(StringUtils.isEmpty(uuid)){
            return null;
        }
        return playerManager.get(uuid);
    }

    /**
     * 从玩家管理器中删除用户
     * @param uuid
     */
    public synchronized void removePlayer(String uuid){
        playerManager.remove(uuid);
    }
}
