# DFLobbyServer
A minimal lobby server written with [Minestom (1.21.8, 2025.10.05)](https://github.com/Minestom/Minestom/tree/2025.10.05-1.21.8)

> [!WARNING]
> NOT INTENDED FOR PERSONAL USE  
> This server is built for a very specific use case, and you may need to modify the source code in order to make this server work for you.  
> I will not help you if you try to use this server. The code here is provided as-is.

## Configuration
Configuration is done via the server.toml file, which is auto-generated upon first boot.  
This server expects a pre-made lobby server, and has an optional nether world.

See [this file](server.toml) for an example configuration, but [this file](src/main/resources/server.toml) for the automatically generated default configuration.

## Dependencies
- DFProxyPlugin (should work without it)
   - for the proxy.expectChannelResponse setting, validating whether clients are considered "valid" and/or on bedrock