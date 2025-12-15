# DFLobbyServer
A minimal lobby server written with [Minestom (1.21.8, 2025.10.05)](https://github.com/Minestom/Minestom/tree/2025.10.05-1.21.8)

> [!WARNING]
> NOT INTENDED FOR PERSONAL USE  
> This server is built for a very specific use case, and you may need to modify the source code in order to make this server work for you.  
> I will not help you if you try to use this server. The code here is provided as-is.

## Configuration
Configuration is done via the server.toml file, which is auto-generated upon first boot.  
This server expects a pre-made lobby world, and has an optional nether world.

See [this file](src/main/resources/server.toml) for the automatically generated default configuration.

## Soft Dependencies
- DFProxyPlugin (should work without it)
   - for the proxy.expectChannelResponse setting, validating whether clients are considered "valid" and/or on bedrock
   - this project makes use of the full [v0.0.0-INDEV spec](https://github.com/df-mcserver/DFProxyPlugin/blob/ef65355cbf245b5e3d89dfb4ac48a9ea04d9f5b9/PLUGIN_SPEC.md).