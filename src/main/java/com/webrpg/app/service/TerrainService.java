package com.webrpg.app.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

import com.webrpg.app.model.derived.RiverMap;
import com.webrpg.app.model.derived.Point;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class TerrainService {

    private BidiMap<String, Integer> terrMap = new DualHashBidiMap<>();
    private BidiMap<String, Integer> terrNameToID = new DualHashBidiMap<>();
    private BidiMap<Integer, Integer> terrColorToID = new DualHashBidiMap<>();
    private TerrainSelector[][] selectorBox = new TerrainSelector[3][3];
    private double adjOrthWeight;
    private double adjCornWeight;
    private double generalWeight;
    private Map<Integer, Integer> topoMap;
    private int expo = 10;  //for our point conversion to key values for hashmap use

    private String[] terrNames = {
            "BEACH", "BOG", "CLEARED", "DESERT", "ESTUARY", "FARMLAND", "CON_FOREST", "DEC_FOREST", "GLACIER", "GRASSLAND", "JUNGLE",
            "LAKE", "LAVA", "MARSH", "OCEAN", "RAPIDS", "REEF", "RIVER", "ROAD", "ROCKY", "SALT_LAKE", "SAND_DUNES", "SCORCHED_EARTH", "SILT_BED",
            "SINK_HOLE", "SWAMP", "VOLCANIC"
    };

    public int[] terrColors = {0xFFD700, 0x20B2AA, 0xD3D3D3, 0xF5DEB3, 0x008080, 0x6B8E23, 0x006400, 0x008000,
            0x00BFFF, 0x9ACD32, 0x7CFC00, 0x0000FF, 0x800000, 0x3CB371, 0x00008B, 0x8B008B, 0x00CED1,
            0x0054A6, 0xFFF5EE, 0xA9A9A9, 0xB0C4DE, 0xF0E68C, 0x663300, 0xF5F5DC, 0x404040, 0x8FBC8F, 0xB22222};

    /*
    Checking Github push capabilities with a simple file change as comment
     */
    /*
    Below is the RGB values for the map elevations gradiant, starting with 0 and increasing incrementally by 100 up to 30000.  In the future, for elevations above 30K feet, will use
    Alpha value as a multiplier,so 1x for 0-30k, 2x for 30100 to 60K, etc.  Same for the seaTopoColors only in reverse (depth measurements)
     */
    public int[] topoColors = {744734,1929010,2849086,3506246,4032077,4492114,4951895,5280346,5608798,5871713,6134372,6397030,6594153,6856811,7053933,7185519,7382384,7513970,
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
            16117188,16117189,16117190,16182979,16182980,16182981,16182982,16182983,16248515,16248516,16248517,16248518,16248519,16314052,16314308 };

    public int[] seaTopoColors = { 7259388,7193337,7127287,7061493,6995443,6929393,6863599,6797549,6731755,6665448,6599654,6533604,6467810,6401760,6335966,6269916,6204122,6137815,6072021,
            6005971,5940177,5874127,5808077,5742283,5676233,5610439,5544132,5478338,5412288,5346494,5280444,5214650,5148600,5148342,5016499,4950705,4884655,4818861,4752811,4752297,4686503,
            4620453,4488866,4422816,4357022,4356508,4290714,4224664,4158870,4092820,4027026,3960719,3894925,3828875,3763081,3697031,3630981,3565187,3499137,3433086,3367036,3301242,3235192,
            3169398,3103348,3037554,2971504,2905453,2839403,2773609,2707559,2641765,2641251,2575201,2509407,2443357,2377306,2311256,2245462,2179412,2113618,2047568,1981774,1915724,1849673,
            1783623,1717829,1651779,1585985,1585471,1519421,1453627,1387576,1321526,1255476,1189682,1123632,1057838,991788,991530,925480};



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
    }



    //public void testPseudoRandSeq(Long seed) {
    //    SecureRandom rand = new SecureRandom(seed.toString().getBytes());
    //    for (int i = 0; i < 100; i++) {
    //        System.out.println(rand.nextDouble());
    //    }
   // }


    public void testAlphaValues(String fileName) throws IOException {
        BufferedImage mapImage;
        Color terrainColor;
        mapImage = ImageIO.read(new File(fileName));
        int width = mapImage.getWidth();           //Width we want for our small map generated image
        int height = mapImage.getHeight();          //Height we want for our small map generated image
        //Nested loop for x and y coordinates
        int[][] mapPixels = new int[width][height];
        for (int h = 0; h < height; h++) {
            System.out.print("Row-" + h + ":" );
            for (int w = 0; w < width; w++) {
                //Load Terrain (color) values into pixel array
                terrainColor = new Color(mapImage.getRGB(w, h),true);     //strip off the alpha, leaves only RGB
                mapPixels[w][h] = terrainColor.getRGB();
                System.out.print(terrainColor.getAlpha() + " ");
            }
            System.out.println();
        }
    }

    public void createSeaGradientPalette() throws IOException {
        int color = 0x000000; //Black Default
        //Producing usable Palette, 10 pixels high for each color entry
        BufferedImage img = new BufferedImage(100, seaTopoColors.length*10, BufferedImage.TYPE_INT_RGB);
        int bandY;

        for (int y = 0; y < seaTopoColors.length; y++) {
            //8 bands of color
            bandY = y*10;
            color = seaTopoColors[y];
            for (int offset = 0; offset <8; offset++) {
                for (int x = 0; x < 100; x++) {
                    img.setRGB(x, bandY+offset, color);
                }
            }
            //2 bands of black border
            color = 0x000000;
            for (int offset = 8; offset <10; offset++) {
                for (int x = 0; x < 100; x++) {
                    img.setRGB(x, bandY + offset, color);
                }
            }
        }
        File f = new File("C:\\development\\maps\\SeaTopoColorGradient.png");
        ImageIO.write(img, "png", f);
    }

    public void createGradientPalette() throws IOException {
        int color = 0x000000; //Black Default
        //Producing usable Palette, 10 pixels high for each color entry
        BufferedImage img = new BufferedImage(100, topoColors.length*10, BufferedImage.TYPE_INT_RGB);
        int bandY;

        for (int y = 0; y < topoColors.length; y++) {
            //8 bands of color
            bandY = y*10;
            color = topoColors[y];
            for (int offset = 0; offset <8; offset++) {
                for (int x = 0; x < 100; x++) {
                    img.setRGB(x, bandY+offset, color);
                }
            }
            //2 bands of black border
            color = 0x000000;
            for (int offset = 8; offset <10; offset++) {
                for (int x = 0; x < 100; x++) {
                    img.setRGB(x, bandY + offset, color);
                }
            }
        }
        File f = new File("C:\\development\\maps\\TopoColorGradient.png");
        ImageIO.write(img, "png", f);
    }

    public int analyzeRivers(String bigFileName, int bigXPos, int bigYPos, int smallWidth, int smallHeight) throws IOException {
        //bigFileName = "C:\\development\\maps\\faerun.6.10.small.gif";
        Map<Integer, RiverMap> riverMaps = new HashMap<>();
        int[][] pixelBox = new int[3][3];   //holds pixel colors of adjacent orthogonal and diagonal terrain
        BufferedImage bigMapImage, smallMapImage;
        Integer terrainColor = 0x000000;
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
                    /*
                    Rather than create a multidimensional hashmap to key against a Point, using some math to create the
                    key from x, and y value, in this case int key = i*E + j.  It follows that i=key/E and j=key%E
                     */
                    riverMaps.put(w*expo+h,new RiverMap(new Point(w,h)));
                }
            }
        }
        /*
        Now that we've scanned the map for river instances, iterate through our river Hashmap and complete River Object
        which includes setting endpoint flag, entry and exit points. We are not generating the terrain in this function,
        but this River terrain information will be used when the tactical terrain does need to be generated in the future.
         */
        //Flag all the rivers first
        for (RiverMap r : riverMaps.values()){
            //check each river point in relation to surrounding points and determine endpoint status and exit,entry points
            //1.get pixel box for current river point - test if origin, destination, or pass-through river point
            //conditions - connected to estuary point = destination; river with only one (or none) connecting river point AND
            //not connected to estuary point = origin; if not the other 2, the equals a pass-through
            pixelBox = getPixelBox(bigMapPixels,r.getPoint().getX(),r.getPoint().getY(),bigWidth,bigHeight);
            r.setType(setRiverEndPointFlag(pixelBox));
        }
        //Now
            return 1;
    }


    public int setRiverEndPointFlag(int[][] pixelBox){
        //get the pixelBox for this point in the map
        int estuary = 2;    //destination of river
        int origin = 1;     //origin of river flow
        int connector = 0;  //this is a flow through point
        int adjRivers = 0;   //count of adjacent River points - needed to determine origin versus flow through connector
        //blank out middle point, we are only testing what adjacent to define it
        pixelBox[1][1] = 0;
        for (int x = 0; x < pixelBox.length; x++) {
            for (int y = 0; y < pixelBox[0].length; y++) {
                    if (terrMap.getKey(pixelBox[x][y]).equals("ESTUARY")) return estuary;  //we have a 'destination' map
                    if (terrMap.getKey(pixelBox[x][y]).equals("RIVER")) adjRivers+=1;
                }
            }
        //Below because we define an 'origin' river point as one with only one adjacent river point (for simplicity)...
        if (adjRivers > 1) return connector;
        return origin;  //we have an origin, since it was not adj to estuary and no more than 1 other river point
    }

    //bigXPos, bigYPos is the position of regional(biggie) section on the world map; smallWidth and smallHeight set the
    //size of the tactical (small) map to be generated from each point (pixel) we read on the biggie map. In our first case
    //since we scaled the world map to 1 pixel = 5000 ft, and if we consider each "point" on our small map equals 5 feet
    // (standard for many tactical tabletop square dimensions) then we will need the small map to be 5000/5 = 1000 pixels squared
    public int generateTerrain(String bigFileName, int bigXPos, int bigYPos, int smallWidth, int smallHeight) throws IOException {
        //bigFileName = "C:\\development\\maps\\faerun.6.10.small.gif";
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
            {950, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 150, 0},
            {0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 700, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 50, 0, 100, 0, 0, 0},
            {0, 0, 0, 0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 900, 0, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 900, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 100, 800, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 100, 0, 850, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0},
            {100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0},
            {50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 50, 0, 50, 0, 0, 0, 0, 0, 0, 0},
            {50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 950, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0,900, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 50, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 900, 0, 0, 0, 50, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 900, 0, 0, 50, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 950, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 75, 0, 0, 900, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 900, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 950, 0, 0},
            {0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 700, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 950}
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

