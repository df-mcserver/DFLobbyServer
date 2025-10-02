# DFLobbyServer
A minimal lobby server written with Minestom (1.21.8)

> [!WARNING]
> NOT INTENDED FOR PERSONAL USE  
> This server is built for a very specific use case, and you may need to modify the source code in order to make this server work for you.  
> I will not help you if you try to use this plugin. The code here is provided as-is.

## Configuration
Configuration is done via the server.toml file, which is auto-generated upon first boot.  
This server expects a pre-made lobby server. It will not generate any chunks.  
You will have to provide your own lobby world. By default it should be in the world/ folder.

## Dependencies
- DFProxyPlugin (should work without it)
   - for the proxy.expectChannelResponse setting.