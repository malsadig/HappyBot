package org.happy.util.cleverbot;

public interface ChatterBotSession {

    ChatterBotThought think(ChatterBotThought thought) throws Exception;

    String think(String text) throws Exception;
}