package com.webrpg.app.service;

import com.webrpg.app.model.derived.ImageFile;
import com.webrpg.app.model.derived.Point;
import com.webrpg.app.model.derived.RiverMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.*;

public class TerrainService {

    private BidiMap<String, Integer> terrMap = new DualHashBidiMap<>();
    private BidiMap<String, Integer> terrNameToID = new DualHashBidiMap<>();
    private BidiMap<Integer, Integer> terrColorToID = new DualHashBidiMap<>();
    private TerrainSelector[][] selectorBox = new TerrainSelector[3][3];
    private double adjOrthWeight;
    private double adjCornWeight;
    private double generalWeight;
    private List<ImageFile> imageFiles;
    private int mapYDimension;
    private Map<Integer, Integer> topoMap;
    private BidiMap<String,String> terrainFiles = new DualHashBidiMap<>(); ;
    private int expo = 10000;  //for our point conversion to key values for hashmap use
    private double maxRiverWidth = .99;    //The maximum width of a river edge as percentage of tactical map edge length
    private double minRiverWidth = .10;    //The maximum width of a river edge as percentage of tactical map edge length
    //Constants
    private static final int RIVER_ORIG= 1;
    private static final int RIVER_DEST = 2;
    private static final int RIVER_CONN = 0;
    private static final int NORTH= 1;
    private static final int EAST= 10;
    private static final int SOUTH= 100;
    private static final int WEST= 1000;
    //For debug use
    private int riverPointsTraversed;
    private  Map<Integer,Integer> travPoints = new HashMap<>();

    private String[] terrNames = {
            "BEACH", "BOG", "CLEARED", "DESERT", "ESTUARY", "FARMLAND", "CON_FOREST", "DEC_FOREST", "GLACIER", "GRASSLAND", "JUNGLE",
            "LAKE", "LAVA", "MARSH", "OCEAN", "RAPIDS", "REEF", "RIVER", "ROAD", "ROCKY", "SALT_LAKE", "SAND_DUNES", "SCORCHED_EARTH", "SILT_BED",
            "SINK_HOLE", "SWAMP", "VOLCANIC","RIVER_SOURCE"};

    //Moving the following to a file load
    private String[] orientedTerrains = {"BEACH","LAKE","RIVER","RIVER_SOURCE","ROAD"};
    private String[] beachOriented = {"BEACHN","BEACHE","BEACHS","BEACHW","BEACHNE","BEACHES","BEACHSW","BEACHNW","BEACHNES","BEACHESW","BEACHNSW","BEACHNEW","BEACHNESW"};
    private String[] lakeOriented = {"LAKEN","LAKEE","LAKES","LAKEW","LAKENE","LAKEES","LAKESW","LAKENW","LAKENES","LAKEESW","LAKENSW","LAKENEW","LAKENESW"};
    private String[] orientedFileNames ={
      "BeachN_25k25k.jpg","WaterNS_25k25k.jpg","WaterNEW_25k25k.jpg","NONE",
            "WaterN_25k25k.jpg","WaterNS_25k25k.jpg","WaterNEW_25k25k.jpg","Water_25k25k.jpg",
    };

    private String[] roadOverlays = {"STRAIGHT","NEJUNCTION","NESJUNCTION","NESWJUNCTION"};
    private String[] riverOverlays = {"TINY2SMALL","TINY2TINY","SMALL2MED","SMALL2SMALL","SMALL2MED",
                                        "MED2SMALL","MED2MED","MED2LARGE","LARGE2MED","LARGE2LARGE","LARGE2XlARGE",
                                        "xLARGE2lARGE","XLARGE2XLARGE"};


    public int[] terrColors = {0xFFD700, 0x20B2AA, 0xD3D3D3, 0xF5DEB3, 0x008080, 0x6B8E23, 0x006400, 0x008000,
            0x00BFFF, 0x9ACD32, 0x7CFC00, 0x0000FF, 0x800000, 0x3CB371, 0x00008B, 0x8B008B, 0x00CED1,
            0x0054A6, 0xFFF5EE, 0xA9A9A9, 0xB0C4DE, 0xF0E68C, 0x663300, 0xF5F5DC, 0x404040, 0x8FBC8F, 0xB22222, 0x022B52};

    /*
    Checking Github push capabilities with a simple file change as comment
     */
    /*
    Below is the RGB values for the map elevations gradiant, starting with 0 and increasing incrementally by 100 up to 30000.  In the future, for elevations above 30K feet, will use
    Alpha value as a multiplier,so 1x for 0-30k, 2x for 30100 to 60K, etc.  Same for the seaTopoColors only in reverse (depth measurements)
     */
    ArrayList<Integer> topoColors = new ArrayList<Integer>(Arrays.asList(744734,1929010,2849086,3506246,4032077,4492114,4951895,5280346,5608798,5871713,6134372,6397030,6594153,6856811,7053933,7185519,7382384,7513970,
            7710836,7842165,7973750,8105080,8236409,8367738,8499068,8630397,8696190,8827519,8893312,9024641,9090434,9221507,9287300,9418629,9484421,9549958,9615751,9747080,9812616,
            9878409,9944202,10009739,10075531,10141068,10206861,10272653,10338190,10403983,10469519,10535312,10600848,10666641,10732177,10797970,10797971,10863763,10929300,10995092,
            11060629,11060630,11126431,11191958,11257750,11257751,11323287,11389080,11389081,11454873,11520409,11520410,11586202,11651738,11651739,11717531,11783068,11783069,11848860,
            11848861,11914397,11980189,11980190,12045726,12045982,12111519,12111520,12177311,12242848,12242850,12308384,12308641,12374177,12374178,12439970,12439972,12505506,12505507,
            12571299,12571300,12636836,12636837,12637092,12702628,12702629,12768165,12768421,12833958,12833959,12899494,12899750,12899751,12965287,12965288,13031079,13031080,13096616,
            13096617,13096618,13162409,13162410,13227945,13227946,13227947,13293738,13293739,13359274,13359275,13359531,13425067,13425068,13425069,13490604,13490605,13490860,13556396,
            13556397,13621933,13621934,13622189,13687725,13687726,13687727,13753262,13753518,13753519,13819055,13819056,13819057,13884591,13884847,13884848,13950384,13950385,13950386,
            14015920,14016177,14016178,14016179,14081713,14081714,14081715,14147506,14147507,14147508,14213042,14213043,14213044,14213045,14278835,14278836,14278837,14344371,14344372,
            14344373,14410164,14410165,14410166,14410167,14475701,14475702,14475703,14475957,14541493,14541494,14541495,14607030,14607031,14607032,14607286,14672822,14672823,14672824,
            14672825,14738359,14738360,14738361,14738615,14804152,14804153,14804154,14804155,14869688,14869689,14869690,14869945,14935481,14935482,14935483,14935485,15001017,15001018,
            15001019,15001274,15066810,15066811,15066812,15066813,15132346,15132347,15132348,15132603,15198139,15198140,15198141,15198142,15263675,15263676,15263677,15263678,15263932,
            15329468,15329469,15329470,15329471,15395005,15395006,15395007,15395008,15460797,15460798,15460799,15460800,15460801,15526334,15526335,15526336,15526337,15591870,15592126,
            15592127,15592128,15592129,15657663,15657664,15657665,15657666,15657667,15723199,15723455,15723456,15723457,15788992,15788993,15788994,15788995,15788996,15854528,15854529,
            15854785,15854786,15854787,15920321,15920322,15920323,15920324,15985857,15985858,15985859,15985860,15986114,16051650,16051651,16051652,16051653,16051654,16117186,16117187,
            16117188,16117189,16117190,16182979,16182980,16182981,16182982,16182983,16248515,16248516,16248517,16248518,16248519,16314052,16314308));

    public Integer[] seaTopoColors = { 7259388,7193337,7127287,7061493,6995443,6929393,6863599,6797549,6731755,6665448,6599654,6533604,6467810,6401760,6335966,6269916,6204122,6137815,6072021,
            6005971,5940177,5874127,5808077,5742283,5676233,5610439,5544132,5478338,5412288,5346494,5280444,5214650,5148600,5148342,5016499,4950705,4884655,4818861,4752811,4752297,4686503,
            4620453,4488866,4422816,4357022,4356508,4290714,4224664,4158870,4092820,4027026,3960719,3894925,3828875,3763081,3697031,3630981,3565187,3499137,3433086,3367036,3301242,3235192,
            3169398,3103348,3037554,2971504,2905453,2839403,2773609,2707559,2641765,2641251,2575201,2509407,2443357,2377306,2311256,2245462,2179412,2113618,2047568,1981774,1915724,1849673,
            1783623,1717829,1651779,1585985,1585471,1519421,1453627,1387576,1321526,1255476,1189682,1123632,1057838,991788,991530,925480};


    public TerrainService(int mapYDimension){
        this();
        this.mapYDimension = mapYDimension;
    }

    public TerrainService() {
        for (int id = 0; id < terrNames.length; id++) {
            terrMap.put(terrNames[id], terrColors[id]);
            terrNameToID.put(terrNames[id], id);
            terrColorToID.put(terrColors[id], id);
        }
        topoMap = new LinkedHashMap<>();
        //There are 300 entries for topoColors, so incrementing by 100 gives range of 0 to 30K for elevation
        //for (int m = 0; m <= topoColors.length; m++){
        //    topoMap.put(topoColors[m],m*100);
       // }

        adjOrthWeight = 0.50;           //weight given to orthographically adjacent map points
        adjCornWeight = 0.025;          //weight given to diagonally adjacent map points
        generalWeight = 0.035;

        imageFiles = new ArrayList<>();
        imageFiles = loadImageFiles("imageFile.csv");
        for (ImageFile f: imageFiles){
            terrainFiles.put(f.getKey(),f.getFileName());
        }
    }

    private List loadImageFiles(String s) {
        List<ImageFile> imageFiles = new ArrayList();
        Path pathToFile = Paths.get(s);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            // read the first line from the text file
            String line = br.readLine();
            // loop until all lines are read
            while (line != null) {
                // use string.split to load a string array with the values from, using a comma as the delimiter
                String[] attributes = line.split(",");
                ImageFile imageFile = new ImageFile(attributes);
                // adding imageFile into
                imageFiles.add(imageFile);
                // read next line before looping -if end of file reached, line would be null
                line = br.readLine();
        }
    } catch (IOException ioe) { ioe.printStackTrace(); }
        return imageFiles;
    }

    public void testImageDrawOverlay(String target, String source) throws IOException {
        BufferedImage imgTarget = ImageIO.read(new File(target));
        BufferedImage imgSource = ImageIO.read(new File(source));
        Graphics2D g = (Graphics2D) imgTarget.getGraphics();
        g.drawImage(imgSource, 10, 10, null);
        g.drawImage(imgSource,0,0,899,600,0,0,1772,1160,null);
        File newImageFile = new File("C:\\development\\maps\\newOverLay.png");
        ImageIO.write(imgTarget, "png", newImageFile);
    }

    /*check each river point in relation to surrounding points and determine inflow and outflow for individual river
    terrain maps; we are going to use elevation now to determine flow direction and entry and exit edges for each map
     */
    public int analyzeRivers(String bigMapFileName, String elevationMapFile, int bigXPos, int bigYPos, int smallWidth, int smallHeight) throws IOException {
        Map<Integer, RiverMap> riverMaps = new HashMap<>();
        int[][] pixelBox = new int[3][3];   //holds pixel colors of adjacent orthogonal and diagonal terrain
        BufferedImage bigMapImage, elevMapImage;
        Integer terrainColor = 0x000000;
        RiverMap tempRiverMap = new RiverMap();
        //Load image Files
        bigMapImage = ImageIO.read(new File(bigMapFileName));
        elevMapImage = ImageIO.read(new File(elevationMapFile));
        //Get width and height (x and y) for the bigMap and evelMap images
        int bigWidth = bigMapImage.getWidth();
        int bigHeight = bigMapImage.getHeight();
        //Nested loop for x and y coordinates
        int[][] bigMapPixels = new int[bigWidth][bigHeight];
        int [][] elevation = new int[bigWidth][bigHeight];

        //Get and store elevation pixels
        for (int h = 0; h < bigHeight; h++) {
            for (int w = 0; w < bigWidth; w++) {
                //Load elevation (color) values into pixel array
                terrainColor = (elevMapImage.getRGB(w, h) & 0x00FFFFFF);     //strip off the alpha, leaves only RGB
                //System.out.println(topoColors.indexOf(terrainColor) * 100 + " elevation for: " + w + "," + h);
                elevation[w][h] = topoColors.indexOf(terrainColor) * 100;   //topo array holds array of 300 colors that represent elevation from 0 to 30K feet
            }
        }

        for (int h = 0; h < bigHeight; h++) {
           for (int w = 0; w < bigWidth; w++) {
                //Load Terrain (color) values into pixel array
                terrainColor = (bigMapImage.getRGB(w, h) & 0x00FFFFFF);     //strip off the alpha, leaves only RGB
                bigMapPixels[w][h] = terrainColor;
                //If we have a river point, this will require special processing. Collect these points for later work
                if (terrMap.getKey(bigMapPixels[w][h]).contains("RIVER")){
                    /*
                    Rather than create a multidimensional hashmap to key against a Point, using some math to create the
                    key from x, and y value, in this case int key = i*E + j.  It follows that i=key/E and j=key%E
                     */
                    riverMaps.put(w*expo+h,new RiverMap(new Point(w,h)));
                    tempRiverMap = riverMaps.get(w*expo + h);
                    tempRiverMap.setElevation(elevation[w][h]);
                    if (terrMap.getKey(bigMapPixels[w][h]).equals("RIVER_SOURCE"))
                        tempRiverMap.setType(RIVER_ORIG);
                }
            }
        }
        /* May not be required as we are setting Origin points by color
        int loopflag = 0;
        for (RiverMap r : riverMaps.values()){
            loopflag++;
            pixelBox = getPixelBox(bigMapPixels,r.getPoint().getX(),r.getPoint().getY(),bigWidth,bigHeight);
            System.out.println("Loop: " + loopflag + " Setting River enpoint for point " + r.getPoint().getX() + "," + r.getPoint().getY());
            r.setType(setRiverEndPointFlag(pixelBox));
        }
         */

        //Now we iterate again to find the origin points and traverse the river to either a river point with destination
        // OR we hit another River point that already has a parent
        riverPointsTraversed = 0;
        for (RiverMap r : riverMaps.values()){
            if (r.getType() == RIVER_ORIG){  //we have a river origin
                System.out.println("Traversing river from Origin point " + r.getPoint().getX() + "," + r.getPoint().getY());
                traverseRiver(r, bigMapPixels,riverMaps,smallWidth);
            }
        }
        int pointTest = 0;
        for (RiverMap r : riverMaps.values()){
            pointTest = r.getPoint().getX()*expo+ r.getPoint().getY();
            if (!travPoints.containsValue(pointTest)){  //we have a river origin
                System.out.println("This point was not traversed: " + r.getPoint().getX() + "," + r.getPoint().getY());
            }
        }

        System.out.println("River Points Traversed: " + riverPointsTraversed);
        //We need to serialize this HashMap to load the information back when we need to generate terrain on the fly.
            return 1;
    }

    private void traverseRiver(RiverMap curRM, int[][] bigMapPixels, Map<Integer, RiverMap> riverMaps, int mapWidth) {
        riverPointsTraversed++; //for debug
        SecureRandom rand = new SecureRandom();
        Point edgePoint = new Point();
        int bigWidth = bigMapPixels.length;
        int bigHeight = bigMapPixels[0].length;
        int riverX = curRM.getPoint().getX();
        int riverY = curRM.getPoint().getY();
        int curElevation = curRM.getElevation();
        int edgeWidth;
        travPoints.put(riverPointsTraversed,riverX*expo+riverY);
        int[][] pixelBox = new int[3][3];
        //get the pixelBox for this RiverMap Object
        pixelBox = getPixelBox(bigMapPixels,riverX,riverY,bigWidth,bigHeight);
        pixelBox[0][0] = 0;   //this is the current Point, not need to test for it
        //Loop through pixel box - for each adjacent River without a parent, add that adjacent River
        for (int x = 0; x < pixelBox.length; x++) {
            for (int y = 0; y < pixelBox[0].length; y++) {
                //We just want orthogonally adjacent  river points to connect
                if (((x+y)&1)!=0 && pixelBox[x][y] !=0) {  //for a 3,3 matrix, adjacent orthogonals are (1,0), (0,1), (2,1), (1,2)
                    if (terrMap.getKey(pixelBox[x][y]).equals("RIVER")) {
                        int adjX = riverX + x - 1;
                        int adjY = riverY + y - 1;
                        RiverMap adjRiver = riverMaps.get(adjX * expo + adjY);
                        int adjElevation = adjRiver.getElevation();
                        //adjRivers at lower elevations OR at same elevation AND don't have Parent are added as a child point
                        if (adjElevation < curElevation || (adjElevation==curElevation && adjRiver.getParents().isEmpty())){
                            //***add adjRiver to child list for current River Point
                            curRM.getChildren().add(adjRiver);  //current River point adds a child
                            adjRiver.getParents().add(curRM);   //adjacent  adds the current River Point as a parent
                            //***add outflow edge aligned to adj Child
                            edgeWidth = (int) (mapWidth * (rand.nextDouble()*(maxRiverWidth-minRiverWidth)+ minRiverWidth)); //ok to lose precision here
                            int axisValue = (mapWidth-edgeWidth)/2; //ok to lose precision here. Effectively splits distance from map edge to each side of river equally
                            switch(x*10+y){
                                case 10: curRM.addEdge(new Point(axisValue,0),edgeWidth,RiverMap.NORTH); break;               // (1.0) is Northern adj. Child
                                case 21: curRM.addEdge(new Point(mapWidth-1,axisValue),edgeWidth,RiverMap.EAST); break;       // (2.1) is Eastern adj. Child
                                case 12: curRM.addEdge(new Point(axisValue,mapWidth-1),edgeWidth,RiverMap.SOUTH); break;      // (1.2) is Southern adj. Child
                                case 1: curRM.addEdge(new Point(0,axisValue),edgeWidth,RiverMap.WEST); break;                 // (0,1) is western adj child
                                default: System.out.println("Error: Not an orthogonally adjacent River point"); break;
                            }
                            //Now recursive call on Child, traversing down this river path until we hit an end point or the edge of map. The varies
                            // branches from the origin
                            System.out.println("Traversing river from child point " + adjRiver.getPoint().getX() + "," + adjRiver.getPoint().getY());
                            traverseRiver(adjRiver, bigMapPixels, riverMaps,mapWidth);
                        }
                    }
                }
            }
        }
    }


    public int setRiverEndPointFlag(int[][] pixelBox){
        //get the pixelBox for this point in the map
        int adjRivers = 0;   //count of adjacent River points - needed to determine origin versus flow through connector
        //blank out middle point, we are only testing what adjacent to define it
        pixelBox[1][1] = 0;
        for (int x = 0; x < pixelBox.length; x++) {
            for (int y = 0; y < pixelBox[0].length; y++) {
                if (((x+y) & 1)!= 0 && pixelBox[x][y] != 0) {  //for a 3,3 matrix, adjacent orthogonals are (1,0), (0,1), (2,1), (1,2)
                    if (terrMap.getKey(pixelBox[x][y]).equals("ESTUARY"))
                        return RIVER_DEST;  //we have a 'destination' map
                    if (terrMap.getKey(pixelBox[x][y]).equals("RIVER")) adjRivers += 1;
                }
                }
            }
        //Below because we define an 'origin' river point as one with only one adjacent river point (for simplicity)...
        if (adjRivers > 1) return RIVER_CONN;
        return RIVER_ORIG;  //we have an origin, since it was not adj to estuary and no more than 1 other river point
    }
    public int generateTacticalMap(String bigFileName, int xPos, int yPos, int smallWidth, int smallHeight) throws IOException {
        //Calculate unique random seed for this tactical map generation
        double randSeed = getTerrainSeed(xPos,yPos);
        //Lay background terrain for tactical ( or “small”) map This is now a large 25Kx25k pixel jpg
        BufferedImage bigMapImage, smallMapImage,bgImage ;
        bigMapImage = ImageIO.read(new File(bigFileName));
        int[][] pixelBox = getPixelBox(bigMapImage,xPos,yPos);
        int terrainColor = pixelBox[1][1];     //strip off the alpha, leaves only RGB
        //        -	If shoreline map (lake or ocean shore) orient correct map background
        bgImage = loadBackGroundImage(pixelBox);
        // If river map, assemble river tiles according to river network size and direction
        //        -	Load river map file corresponding to river map coordinate for this small map
        //-	complementary background image objects)
        //For Road, shoreline (ocean or lake shore), or river calculate (or have predetermine) remaining allowable land regions of map for map enhancement
        //Loop through map coordinates (0<=x<1000; 0<=y<1000)
        //Lay objects (shrubs, rocks, flowers, ground patches, alternate tile, etc)
        //EndLoop
        //If Forest (deciduous, conifer, jungle)…
        //…Loop through map coordinates (0<=x<1000; 0<=y<1000)
        //Lay trees by forest type and probability
        //        EndLoop
        return 1;
    }

    private BufferedImage loadBackGroundImage(int[][] pixelBox) {
        int terrainColor = pixelBox[0][0];
        int mapTerrain = pixelBox[1][1];
        int rotation = 0;
        List<String> dirTerrains = Arrays.asList("Beach","Lake","Road","River");
        String fileName = terrMap.getKey(terrainColor);
        //for an expanded version we need to review the type of terran and possibly the surrounding terrain to determine
        //actual type of background terrain. For now use grassland background or stock maps
        if(dirTerrains.contains(terrMap.getKey(terrainColor))){
            String oriented = getWaterOrientation(pixelBox);
            fileName.concat(oriented);
            switch (oriented) {
                case "N": break;
                case "E": rotation = 90;    break;
                case "S": rotation = 180;   break;
                case "W": rotation = 180;   break;
                case "NE": rotation = 0;    break;
                case "NS": rotation = 0;    break;
                case "NW": rotation = 270;  break;
                case "NEW": rotation = 0;   break;
                case "NES": rotation = 90;  break;
                case "NSE": rotation = 180; break;
                case "NESW": rotation = 0;  break;
                case "ES": rotation = 90;   break;
                case "EW": rotation = 90;   break;
                case "ESW": rotation = 180; break;
                case "SW": rotation = 180;  break;
                default: rotation = 0;      break;     //This is a self contained map, and unconnected beach (island) or lake
            }
            System.out.println(rotation);
        }
        return null;
    }

    private String getWaterOrientation(int[][] pixelBox) {
        int orientation = 0;
        int terrainType = pixelBox[1][1];
        String oriented = "";
        if(terrainType==pixelBox[1][0]) oriented.concat("N");
        if(terrainType==pixelBox[2][1]) oriented.concat("E");;
        if(terrainType==pixelBox[1][2]) oriented.concat("S");;
        if(terrainType==pixelBox[0][1]) oriented.concat("W");;

        return oriented;
    }


    //bigXPos, bigYPos is the position of regional(biggie) section on the world map; smallWidth and smallHeight set the
    //size of the tactical (small) map to be generated from each point (pixel) we read on the biggie map. In our first case
    //since we scaled the world map to 1 pixel = 5000 ft, and if we consider each "point" on our small map equals 5 feet
    // (standard for many tactical tabletop square dimensions) then we will need the small map to be 5000/5 = 1000 pixels squared
    public int generateTerrain(String bigFileName, int bigXPos, int bigYPos, int smallWidth, int smallHeight) throws IOException {
        //bigFileName = "C:\\development\\maps\\faerun.6.10.small.gif";
        double randSeed = getTerrainSeed(bigXPos,bigYPos);
        List<RiverMap> riverMaps = new ArrayList<>();
        String fileStub = "C:\\development\\maps\\faerun.";
        int[][] pixelBox = new int[3][3];   //holds pixel colors of adjacent orthogonal and diagonal terrain
        Integer terrainColor = 0x000000;
        int globalXPos = bigXPos * 100;             //The x and y coordinates of the smallMap on the Global (world) map
        int globalYPos = bigYPos * 100;
        BufferedImage bigMapImage, smallMapImage;
        //Load image File
        bigMapImage = ImageIO.read(new File(bigFileName));
        //Get width and height (x and y) for the image
        int bigWidth = bigMapImage.getWidth();
        int bigHeight = bigMapImage.getHeight();
        //Nested loop for x and y coordinates
        int[][] bigMapPixels = new int[bigWidth][bigHeight];
        for (int h = 0; h < bigHeight; h++) {
            for (int w = 0; w < bigWidth; w++) {
                //Load Terrain (color) values into pixel array
                terrainColor = (bigMapImage.getRGB(w, h) & 0x00FFFFFF);     //strip off the alpha, leaves only RGB
                bigMapPixels[w][h] = terrainColor;
                //If we have a river point, this will require special processing. Collect these points for later work
                if (terrMap.getKey(bigMapPixels[w][h]).equals("RIVER")){
                    riverMaps.add(new RiverMap(new Point(w,h)));
                }
            }
        }
        //For each pixel generate subMap (png most likely)
        int tempXPos;
        int tempYPos;
        for (int h = 0; h < smallHeight; h++) {
            for (int w = 0; w < smallWidth; w++) {
                pixelBox = getPixelBox(bigMapPixels, w, h, bigWidth, bigHeight);
                //grab pixel color at (x,y); if needed, convert to proper hex value
                //generate subMap
                tempXPos = globalXPos + w;
                tempYPos = globalYPos + h;
                smallMapImage = calculateTerrainRegion(bigMapPixels[w][h], pixelBox, smallWidth, smallHeight);
                File smallMapFile = new File(fileStub + tempXPos + "." + tempYPos + ".gif");
                ImageIO.write(smallMapImage, "gif", smallMapFile);
                //Store subMap
            }
        }
        //End Loop
        return 1;
    }
    //Separating the seed calc from the terrain generation in case we want to change method
    private int getTerrainSeed(int bigXPos, int bigYPos) {
        //given two numbers between 0 and n, create a unique number for any point in matrix {nxn}
        //may be better way but simply going to shift one coordinate over by max number of digits in second coordinate
        return bigXPos*10^mapYDimension+bigYPos;
    }
    /*
    There needs to be a secondary process after the terran has been calculated that then overlays roads and rivers. Both
    will usually assume continuation to and connectivity with adjacent terrain blocks.
     */

    private BufferedImage calculateTerrainRegion(int terrainType, int[][] pixelBox, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        SecureRandom secRand = new SecureRandom();
        int pixColor = 0x000000;
        int variance = 50;
        int[] imagePixelData = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        generateTerrainMatrix(terrainType, pixelBox);
        int upBndLimit = (int) Math.round(height * .05);
        int upBnd = upBndLimit;
        int lowBndLimit = (int) Math.round(height * .95);
        int lowBnd = lowBndLimit;
        int leftBndLimit = (int) Math.round(width * .05);
        int leftBnd = leftBndLimit;
        int rightBndLimit = (int) Math.round(width * .95);
        int rightBnd = rightBndLimit;

        //First make sure all pixels are filled with something...use main selector
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                pixColor = terrColors[selectorBox[1][1].chooseColor()];
                imagePixelData[h * width + w] = pixColor;
            }
        }

        //Then selectively generate for respective sides
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (h <= upBnd) {
                    pixColor = terrColors[selectorBox[1][0].chooseColor()];
                    imagePixelData[h * width + w] = pixColor;
                    upBnd = secRand.nextInt(variance) + upBndLimit;
                }
                if (h >= lowBnd) {
                    pixColor = terrColors[selectorBox[1][2].chooseColor()];
                    imagePixelData[h * width + w] = pixColor;
                    lowBnd = lowBndLimit - secRand.nextInt(variance);
                }
                if (w <= leftBnd) {
                    pixColor = terrColors[selectorBox[0][1].chooseColor()];
                    imagePixelData[h * width + w] = pixColor;
                    leftBnd = secRand.nextInt(variance) + leftBndLimit;
                }
                if (w >= rightBnd) {
                    pixColor = terrColors[selectorBox[2][1].chooseColor()];
                    imagePixelData[h * width + w] = pixColor;
                    rightBnd = rightBndLimit - secRand.nextInt(variance);
                }
                System.out.println("U:" + upBnd + ",L:" + lowBnd + ",Le:" + leftBnd + ",R:" + rightBnd);
            }
        }
        img.getRaster().setDataElements(0, 0, width, height, imagePixelData);
        //System.out.println(" : Calculated Terrain and returned small map image");
        return img;
    }

    private void generateTerrainMatrix(Integer terrainType, int[][] pixelBox) {
        double tempWeight;
        Integer matrixID = terrColorToID.get(terrainType);
        Integer currID = 0;
        /*
        We are filling a 3x3 array with a TerrainSelector that will be an overlay of the currently considered terrain type
        and some weighted addition of an adjacent terrain type.  Position 0,0 corresponds top left corner, top  0,1 , top
        right corner 0,2, left side 1,0, etc to bottom right corner 2,2.  Corners have the least effect on the current terrain
        calculation and so impose a lower weight than orthogonally adjacent terrain (.05 vs .3 respectively)
         */
        //Step 1: First pass with pixel Box.Generate an aggregated terrain matrix from all valid adjacent sides.  Will be be our
        // base terrain matrix
        selectorBox[1][1] = new TerrainSelector(terrWeight[matrixID], 1000);         //raw Matrix
        for (int y = 0; y < pixelBox[0].length; y++) {
            for (int x = 0; x < pixelBox.length; x++) {
                if (!(pixelBox[x][y] == 0) && !(x == 1 && y == 1)) {       //Apply overlay from outer terrain but not central terrain or non-existent adjacents
                    currID = terrColorToID.get(pixelBox[x][y]);
                    if ((x == y) || (x == 0 && y == pixelBox.length - 1) || (x == pixelBox.length && y == 0)) { //Effectively the corner terrain...
                        selectorBox[1][1].overlaySelector(terrWeight[currID], generalWeight * .1); //Only 10% of orthogonal
                    } else {        //Orthogonally adjacent terrain overlay
                        selectorBox[1][1].overlaySelector(terrWeight[currID], generalWeight);
                    }
                }
            }
        }
        //Step 1: Second pass with pixel Box. Now we apply a heavier weight to the terrain matrix, but specific for the
        //terrain in the corresponding pixel box
        for (int x = 0; x < pixelBox.length; x++) {
            for (int y = 0; y < pixelBox[0].length; y++) {
                if (!(x == 1 && y == 1)) {
                    selectorBox[x][y] = new TerrainSelector(); //Create new TerrainSelector for each pixelbox position
                    selectorBox[x][y].copySelector(selectorBox[1][1]);      //Copy the main TerrainSelector we created above
                }
                if (!(pixelBox[x][y] == 0) && !(x == 1 && y == 1)) {
                    currID = terrColorToID.get(pixelBox[x][y]);
                    if ((x == y) || (x == 0 && y == pixelBox.length - 1) || (x == pixelBox.length && y == 0)) { //Effectively the corner terrain...
                        selectorBox[x][y].overlaySelector(terrWeight[currID], adjCornWeight);
                    } else {
                        selectorBox[x][y].overlaySelector(terrWeight[currID], adjOrthWeight);
                    }
                }
            }
        }
        for (int y = 0; y < pixelBox[0].length; y++) {
            for (int x = 0; x < pixelBox.length; x++) {
                selectorBox[x][y].organizeSelector();
            }
        }

    }

        /*
        for (int x = 0; x<pixelBox.length; x++) {
            for (int y = 0; y < pixelBox[0].length; y++) {
                if (pixelBox[x][y] != 0x000000) {                //test if we have valid value for that adjacent terrain
                    if ((x == y) || (x == 0 && y == pixelBox.length - 1) || (x == pixelBox.length && y == 0)) { //Effectively the corner terrain...
                        tempWeight = tSelect.terrainSelector.get(terrColorToID.get(pixelBox[x][y])) + adjTerrWeight * weightModifier;
                        tSelect.terrainSelector.put(terrColorToID.get(pixelBox[x][y]),tempWeight);                //..and these corners have less impact...
                        tSelect.total += adjTerrWeight * weightModifier;
                    } else {
                        tempWeight = tSelect.terrainSelector.get(terrColorToID.get(pixelBox[x][y])) + adjTerrWeight;
                        tSelect.terrainSelector.put(terrColorToID.get(pixelBox[x][y]),tempWeight);               //...than the orthogonal spaces
                        tSelect.total += adjTerrWeight;
                    }
                }
            }
        }
         */

    //Test if pixel for 8 conditions: 4 corner positions, 4 side positions
    //Because the pixel could pixel coordinates could be at the edges or corner of the big map, some of the positions
    //in our returned int array will not correspond to a color (the position they reference doesnt exist) and we just
    //keep int - 0x0000
    //***Note I dont like this if then logic.  There are redundant tests performed for height
    private int[][] getPixelBox(BufferedImage img,int x, int y ){
        int[][] pixelBox = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};   //prefill with black, which equals "no adjacent pixel at x,y position"
        int maxRight = img.getWidth()-1;      //farthest most x position
        int maxBottom = img.getHeight()-1;    //farthest most y position
        pixelBox[1][1] = (img.getRGB(x, y) & 0x00FFFFFF);          //1.1 position always filled - it is the main selector color
        if (x != 0) {                                              //Test if this is not a left border pixel on the biggy map
            pixelBox[0][1] = (img.getRGB(x-1, y) & 0x00FFFFFF);               //Left center pixel filled
            if (y != 0) {                                                       //Test if this is not a top border pixel on the biggy map
                pixelBox[0][0] = (img.getRGB(x-1, y-1) & 0x00FFFFFF);    //top left corner pixel filled
            }
            if (y != maxBottom) {                                   //Test if this is not a bottom border pixel on the biggy map
                pixelBox[0][2] = (img.getRGB(x-1, y+1) & 0x00FFFFFF);         //bottom left corner pixel filled
            }
        }
        //***test for right positions ***/
        if (x != maxRight) {
            pixelBox[2][1] = (img.getRGB(x+1, y) & 0x00FFFFFF);             //right center pixel filled
            if (y != 0) {                                                      //this is not a top border pixel on the biggy map
                pixelBox[2][0] = (img.getRGB(x+1, y-1) & 0x00FFFFFF);    //top right corner pixel filled
            }   //this is not a top border pixel
            if (y != maxBottom) {                                              //this is not a bottom border pixel on the biggy map
                pixelBox[2][2] = (img.getRGB(x+1, y+1) & 0x00FFFFFF);    //bottom right corner pixel filled
            }
        }
        if (y != 0) {                                                           //this is not a top row pixel on the biggy map
            pixelBox[1][0] = (img.getRGB(x, y-1) & 0x00FFFFFF);              //top center corner pixel filled
        }
        if (y != maxBottom) {                                                   //this is not a bottom row pixel on the biggy map
            pixelBox[1][2] = (img.getRGB(x, y+1) & 0x00FFFFFF);              //bottom center pixel filled
        }
        return pixelBox;
    }

    private int[][] getPixelBox(int[][] bigMapPixels, int w, int h, int bigWidth, int bigHeight) {
        int[][] pixelBox = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};   //prefill with black, which equals "no adjacent pixel at x,y position"
        int maxRight = bigWidth-1;      //farthest most x position
        int maxBottom = bigHeight-1;    //farthest most y position
        //***test for left positions ***/
        pixelBox[1][1] = bigMapPixels[w][h];                      //1.1 position always filled - it is the main selector color
        if (w != 0) {                                              //Test if this is not a left border pixel on the biggy map
            pixelBox[0][1] = bigMapPixels[w - 1][h];                //Left center pixel filled
            if (h != 0) {                                          //Test if this is not a top border pixel on the biggy map
                pixelBox[0][0] = bigMapPixels[w - 1][h - 1];          //top left corner pixel filled
            }
            if (h != maxBottom) {                                   //Test if this is not a bottom border pixel on the biggy map
                pixelBox[0][2] = bigMapPixels[w - 1][h + 1];          //bottom left corner pixel filled
            }
        }
        //***test for right positions ***/
        if (w != maxRight) {
            pixelBox[2][1] = bigMapPixels[w + 1][h];                //right center pixel filled
            if (h != 0) {                                          //this is not a top border pixel on the biggy map
                pixelBox[2][0] = bigMapPixels[w + 1][h - 1];          //top right corner pixel filled
            }   //this is not a top border pixel
            if (h != maxBottom) {                                   //this is not a bottom border pixel on the biggy map
                pixelBox[2][2] = bigMapPixels[w + 1][h + 1];          //bottom right corner pixel filled
            }
        }
        if (h != 0) {                                          //this is not a top row pixel on the biggy map
            pixelBox[1][0] = bigMapPixels[w][h - 1];            //top center corner pixel filled
        }
        if (h != maxBottom) {                                   //this is not a bottom row pixel on the biggy map
            pixelBox[1][2] = bigMapPixels[w][h + 1];          //bottom center pixel filled
        }
        return pixelBox;
    }


    /*
    Given nxn matrix for every "empty" point i,j within matrix, calculate altitude using modified diamond square
    algorithm. Given i,j is empty, search radially starting at distance distance +/- 1 from point, increasing distance r
    until acceptable number of points found (start with >2 points).  Then calculate distance to each point, use that distance
    as the proportional strength of that searched points altitude to calculate altitude of point i,j.
     */
    private void getPixelBox(int[][] mapPixels, int i, int j, int radius) {
        int dim = (radius * 2) +1;  //bounding matrix dimensions
        int[][] selected = new int[dim][dim];
        int width = mapPixels.length;
        int height = mapPixels[0].length;
        //Find closest points
        for (int r = 1; r <= radius; r++) {           //include all concentric boundaries up r from point
            for (int x = -r; x <= r; x++) {           //grab top and bottom rows at j-r and j+r
                if (i + x >= 0 && i + x <= width) {       //point must be within matrix width boundaries
                    if (j - r >= 0) {            //top row of selection box is in matrix
                        selected[x][-r] = mapPixels[i + x][j - r];
                    }
                    if (j + r <= height) {            //bottom row of selection box is in matrix
                        selected[x][r] = mapPixels[i + x][j + r];
                    }
                }
            }
            for (int y = -r+1; y < r; y++) {           //grab far left and far right columns inside corners (height from -r+1 to r-1
                if (j + y >= 0 && i + j <= width) {       //point must be within matrix width boundaries
                    if (j - r >= 0) {            //left cropped column of selection box is in matrix
                        selected[-r][y] = mapPixels[i - r][j + y];
                    }
                    if (j + r <= height) {            //right cropped column of selection box is in matrix
                        selected[r][y] = mapPixels[i + r][j + y];
                    }
                }
            }
        }

        //calculate distance from i,j to each point
        //calculate
    }

    private int convertElevation(int pixelValue){
        return 0;
    }

    double[][] terrWeight = {
            {950, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 150, 0,0},
            {0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 700, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 50, 0, 100, 0, 0, 0,0},
            {0, 0, 0, 0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 900, 0, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 900, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 100, 800, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 100, 0, 850, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0,0},
            {100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0,0},
            {50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0,0},
            {50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0,900, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 50, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 0, 0, 50, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 900, 0, 0, 50, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 950, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 75, 0, 0, 900, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 900, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 950, 0, 0,0},
            {0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 700, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 950,0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0,900, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 50, 0, 0, 0,0},
    };
}


    class TerrainSelector {
        Map<Integer, Double> terrainSelector;
        Map<Integer, Double> trimmedSelector;
        public double total;

        TerrainSelector(){
            terrainSelector = new LinkedHashMap<>();
            trimmedSelector = new LinkedHashMap<>();
        }

        TerrainSelector(double[] terrWeight, double total){
            terrainSelector = new LinkedHashMap<>();
            trimmedSelector = new LinkedHashMap<>();
            loadSelector(terrWeight);
            this.total = total;
        }

        public void loadSelector(double[] terrWeight){
            for (int m = 0; m < terrWeight.length; m++){
                terrainSelector.put(m,terrWeight[m]);
            }
        }
        public void copySelector(TerrainSelector ts){
            this.total = ts.total;
            for (Map.Entry<Integer, Double> entry : ts.terrainSelector.entrySet()) {
                    terrainSelector.put(entry.getKey(),entry.getValue());
            }
        }

        public void  modifySelector(int terrID, double terrValue, double weight){
            double newWeight = 0;
            newWeight = terrainSelector.get(terrID) + terrValue * weight;
            terrainSelector.put(terrID,newWeight);
        }

        public void overlaySelector(double[] overlay, double weight){
            double newValue = 0;
            double increment = 0;
            for(int m=0; m<overlay.length; m++){
                increment = overlay[m]*weight;
                total += increment;
                newValue = terrainSelector.get(m) + increment;
                terrainSelector.put(m,newValue);       //updates the class selector map at key 'm'
            }
        }
        /*
        STrip out zero probability terrains and then arrange the values in hash list as
        Graduated list of 'threshold' probability values.
         */
        public void organizeSelector() {
            double value = 0;
            for (Map.Entry<Integer, Double> entry : terrainSelector.entrySet()) {
                if (entry.getValue() != 0) {
                    trimmedSelector.put(entry.getKey(),entry.getValue());
                }
            }
            for (Map.Entry<Integer, Double> finalEntry : trimmedSelector.entrySet()) {
                    value += finalEntry.getValue() / total;
                    trimmedSelector.put(finalEntry.getKey(), value);
            }
        }

        //This method used when random result is provided
        public int chooseColor(double result){
            for (Map.Entry<Integer, Double> entry : trimmedSelector.entrySet()) {
                if (result <= entry.getValue()){
                    return entry.getKey();
                }
            }
            return -1;
        }
        //This method used when we want the method to 'roll' the result
        public int chooseColor(){
            SecureRandom rand = new SecureRandom();
            double result = rand.nextDouble();
            for (Map.Entry<Integer, Double> entry : trimmedSelector.entrySet()) {
                if (result <= entry.getValue()){
                    return entry.getKey();
                }
            }
            return -1;
        }
    }

