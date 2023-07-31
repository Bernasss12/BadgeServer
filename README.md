# Badge Server  

---
This is a simple project made mostly for personal use and the main goal was for me to challenge myself and learn how to do it.
The idea (still incomplete) was for me to have stylized badges for both minecraft mod providers, Modrinth and CurseForge, while using the (almost) same url.

> Importante Notice: If anyone is thinking of or actively using the service freely hosted at badge.bernasss12.dev, be advised that the format of the url
> queries is still very much still subject to change.

### Usage

`[domain]/[provider]/[modid]/[data]`

~~Currently only Modrinth data fetching is implemented but ideally~~ this is how you'd generate the badge for both services:  

`![modrinth](https://badge.bernasss12.dev/modrinth/better-enchanted-books/downloads)`  
![modrinth](https://badge.bernasss12.dev/modrinth/better-enchanted-books/downloads)  

> Note: currently curseforge only works with the actual ID of the mods.  
> Also only downloads and names options are implemented, only versions and loaders might be implemented before a complete rework on the badges.

`![curseforge](https://badge.bernasss12.dev/curseforge/369122/downloads)`  
![curseforge](https://badge.bernasss12.dev/curseforge/369122/downloads)  

At the moment there is only one style with the provider logo on the left and text on the right, but the plan it to have more flexibility on that as well as
the date displayed.
The available preset data options are:  

`[none] or /download` - displays a shortened version of the mod's download count.  
`/name` - displays the title of the project  
`/versions` - displays the latest 3 minecraft versions (supported by the project)  
`/loaders` - displays the supported mod loaders by the project  
`/licence` - displays the name of the project's licence, if available  

### Disclaimer

This service is currently hosted, but it's not quite ready for use, anything mentioned above is prone (and mostly already planned) to change.  

#### Examples

![example](https://badge.bernasss12.dev/modrinth/better-enchanted-books/downloads)
![example](https://badge.bernasss12.dev/modrinth/better-enchanted-books/name)
![example](https://badge.bernasss12.dev/modrinth/logistics-pipes/downloads)
![example](https://badge.bernasss12.dev/modrinth/logistics-pipes/name)
![curseforge](https://badge.bernasss12.dev/curseforge/369122/downloads)
![example](https://badge.bernasss12.dev/modrinth/better-enchanted-books/versions)
![example](https://badge.bernasss12.dev/modrinth/better-enchanted-books/loaders)
![curseforge](https://badge.bernasss12.dev/curseforge/369122/name)
![example](https://badge.bernasss12.dev/modrinth/better-enchanted-books/licence)
![curseforge](https://badge.bernasss12.dev/curseforge/369122/downloads)
![curseforge](https://badge.bernasss12.dev/curseforge/232838/name)
![curseforge](https://badge.bernasss12.dev/curseforge/232838/downloads)  



