# ArchLibMan Client

[![License](https://img.shields.io/github/license/archjh/ArchLibman?color=blue)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.4-green)](https://www.minecraft.net)
[![Fabric](https://img.shields.io/badge/Fabric-API-blue)](https://fabricmc.net)

A lightweight and modular Minecraft 1.20.4 client built on Fabric loader, designed for utility and quality-of-life enhancements.

![Client Screenshot](screenshot.png) <!-- Add a screenshot later -->

## Features

### Module System
- **Modular architecture** - Easily add/remove features
- **Categories** - Organized module system (Client, Combat, Movement, Render, Player)
- **Runtime configuration** - Toggle modules without restart

### Included Modules
- **ArrayList** - Displays active modules
- **ArmorHUD** - Shows armor status and durability
- **PotionHUD** - Displays active potion effects
- **KeyStrokes** - Visualizes pressed keys with CPS counter
- **TargetHUD** - Shows information about targeted entities
- **FPSDisplay** - Real-time FPS counter
- **Sprint** - Automatic sprinting
- **NoClickDelay** - Removes click delay
- **And more...**

## Installation

1. **Prerequisites**:
   - Minecraft Java Edition 1.20.4
   - [Fabric Loader](https://fabricmc.net/use/)
   - [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

2. **Installation Steps**:
   ```bash
   # Clone the repository
   git clone https://github.com/Archjh/ArchLibman.git
   
   # Build the project (requires JDK 17+)
   ./gradlew build