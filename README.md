# mapbox_prototype
This repository contains the prototype for the Airthings app's map functionality. 
The objective of this project is to display aggregated, location-based sensor data from all Airthings users using Mapbox. 
The project is written in Kotlin, with shared code between Android and iOS for processing raw data. The iOS implementation is currently pending.

## Features
- Display aggregated sensor data on a map.
- Visualize data using 3D extrusions and color coding.
- Utilize shared Kotlin code for data processing between Android and iOS.


## Getting Started
### Prerequisites
- Android Studio
- A Mapbox access token. You can obtain one by signing up on the Mapbox website.

### Installation
Add your Mapbox access token to the local.properties file:
MAPBOX_ACCESS_TOKEN=your_mapbox_access_token