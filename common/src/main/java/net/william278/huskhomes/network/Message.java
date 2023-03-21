package net.william278.huskhomes.network;

import com.google.gson.annotations.Expose;
import net.william278.huskhomes.user.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a message sent by a {@link Broker} cross-server. See {@link #builder()} for
 * a builder to create a message.
 */
public class Message {

    /**
     * Message target indicating all players
     */
    public static final String TARGET_ALL = "ALL";

    @Expose
    private UUID id;
    @Expose
    private Type type;
    @Expose
    private Scope scope;
    @Expose
    private String target;
    @Expose
    private Payload payload;
    @Expose
    private String sender;
    @Expose
    private String sourceServer;

    private Message(@NotNull Type type, @NotNull Scope scope, @NotNull String target, @NotNull Payload payload) {
        this.type = type;
        this.scope = scope;
        this.target = target;
        this.payload = payload;
        this.id = UUID.randomUUID();
    }

    @SuppressWarnings("unused")
    private Message() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public void send(@NotNull Broker broker, @NotNull OnlineUser sender) {
        this.sender = sender.getUsername();
        this.sourceServer = broker.getServer();
        broker.send(this, sender);
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @NotNull
    public Scope getScope() {
        return scope;
    }

    @NotNull
    public String getTarget() {
        return target;
    }

    @NotNull
    public Payload getPayload() {
        return payload;
    }

    @NotNull
    public String getSender() {
        return sender;
    }

    @NotNull
    public String getSourceServer() {
        return sourceServer;
    }

    @NotNull
    public UUID getUuid() {
        return id;
    }

    /**
     * Builder for {@link Message}s
     */
    public static class Builder {
        private Type type;
        private Scope scope = Scope.PLAYER;
        private Payload payload = Payload.empty();
        private String target;

        private Builder() {
        }

        @NotNull
        public Builder type(@NotNull Type type) {
            this.type = type;
            return this;
        }

        @NotNull
        public Builder scope(@NotNull Scope scope) {
            this.scope = scope;
            return this;
        }

        @NotNull
        public Builder payload(@NotNull Payload payload) {
            this.payload = payload;
            return this;
        }

        @NotNull
        public Builder target(@NotNull String target) {
            this.target = target;
            return this;
        }

        @NotNull
        public Message build() {
            return new Message(type, scope, target, payload);
        }

    }

    /**
     * Different types of cross-server messages
     */
    public enum Type {
        TELEPORT_TO_POSITION,
        TELEPORT_TO_NETWORKED_POSITION,
        TELEPORT_REQUEST,
        TELEPORT_TO_NETWORKED_USER,
        TELEPORT_REQUEST_RESPONSE,
        REQUEST_PLAYER_LIST,
        PLAYER_LIST,
        UPDATE_HOME,
        UPDATE_WARP,
        UPDATE_CACHES,
    }

    public enum Scope {
        /**
         * The target is a server name, or "all" to indicate all servers.
         */
        SERVER("Forward"),
        /**
         * The target is a player name, or "all" to indicate all players.
         */
        PLAYER("ForwardToPlayer");

        private final String pluginMessageChannel;

        Scope(@NotNull String pluginMessageChannel) {
            this.pluginMessageChannel = pluginMessageChannel;
        }

        @NotNull
        public String getPluginMessageChannel() {
            return pluginMessageChannel;
        }
    }

}