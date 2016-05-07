

/* Menu list */

/* a convenience function to build a map that contains our key-value pairs which we will 
 * then use to render each item of the menuList.
 */
Map<String, Object> makeItem(int id, String theHeadline, String theSubline, String theCopy, PImage theImage) {
  Map m = new HashMap<String, Object>();
  m.put("headline", theHeadline);
  m.put("subline", theSubline);
  m.put("copy", theCopy);
  m.put("image", theImage);
  m.put("is_on", 0);
  m.put("id", id);
  return m;
}

void menu(int i) {
  println("got some menu event from item with index "+i);
  Checklist[i+1][0]=str(abs(int(Checklist[i+1][0])-1));
}



/* A custom Controller that implements a scrollable menuList. Here the controller 
 * uses a PGraphics element to render customizable list items. The menuList can be scrolled
 * using the scroll-wheel, touchpad, or mouse-drag. Items are triggered by a click. clicking 
 * the scrollbar to the right makes the list scroll to the item correspoinding to the 
 * click-location. 
 */
class MenuList extends Controller<MenuList> {

  float pos, npos;
  int itemHeight = 50;
  int scrollerLength = 30;
  List< Map<String, Object>> items = new ArrayList< Map<String, Object>>();
  PGraphics menu;
  boolean updateMenu;

  MenuList(ControlP5 c, String theName, int theWidth, int theHeight) {
    super( c, theName, 0, 0, theWidth, theHeight );
    c.register( this );
    menu = createGraphics(getWidth(), getHeight() );

    setView(new ControllerView<MenuList>() {

      public void display(PGraphics pg, MenuList t ) {
        if (updateMenu) {
          updateMenu();
        }
        if (inside() ) {
          menu.beginDraw();
          int len = -(itemHeight * items.size()) + getHeight();
          int ty = int(map(pos, len, 0, getHeight() - scrollerLength - 2, 2 ) );
          //menu.fill(255 );
          menu.rect(getWidth()-4, ty, 4, scrollerLength );
          menu.endDraw();
        }
        pg.image(menu, 0, 0);
      }
    }
    );
    updateMenu();
  }

  /* only update the image buffer when necessary - to save some resources */
  void updateMenu() {
    int len = -(itemHeight * items.size()) + getHeight();
    npos = constrain(npos, len, 0);
    pos += (npos - pos) * 0.1;
    menu.beginDraw();
    menu.noStroke();
    menu.background(255, 0 );
    menu.textFont(cp5.getFont().getFont());
    menu.pushMatrix();
    menu.translate( 0, pos );
    menu.pushMatrix();

    int i0 = PApplet.max( 0, int(map(-pos, 0, itemHeight * items.size(), 0, items.size())));
    int range = ceil((float(getHeight())/float(itemHeight))+1);
    int i1 = PApplet.min( items.size(), i0 + range );

    menu.translate(0, i0*itemHeight);

    for (int i=i0; i<i1; i++) {
      Map m = items.get(i);
      menu.fill(255, 180);
      menu.rect(0, 0, getWidth(), itemHeight-1 );
      menu.fill(0);
      menu.textFont(f1);
      menu.text(m.get("headline").toString(), 10, 20 );
      menu.textFont(f2);
      menu.textLeading(12);
      menu.text(m.get("subline").toString(), 10, 35 );
      menu.text(m.get("copy").toString(), 130, 5, 310, 50 );
      menu.image(((PImage)m.get("image")), 433, 5, 40, 40 );
      menu.translate( 0, itemHeight );
    }
    menu.popMatrix();
    menu.popMatrix();
    menu.endDraw();
    updateMenu = abs(npos-pos)>0.01 ? true:false;
  }

  /* when detecting a click, check if the click happend to the far right, if yes, scroll to that position, 
   * otherwise do whatever this item of the list is supposed to do.
   */
  public void onClick() {
    if (getPointer().x()>getWidth()-10) {
      npos= -map(getPointer().y(), 0, getHeight(), 0, items.size()*itemHeight);
      updateMenu = true;
    } else {
      int len = itemHeight * items.size();
      int index = int( map( getPointer().y() - pos, 0, len, 0, items.size() ) ) ;
      setValue(index);
    }
  }

  public void onMove() {
  }

  public void onDrag() {
    npos += getPointer().dy() * 2;
    updateMenu = true;
  } 

  public void onScroll(int n) {
    npos += ( n * 4 );
    updateMenu = true;
  }

  void addItem(Map<String, Object> m) {
    items.add(m);
    updateMenu = true;
  }

  void setItem(int i, Map<String, Object> m) {
    items.set(i, m);
    updateMenu = true;
  }


  Map<String, Object> getItem(int theIndex) {
    return items.get(theIndex);
  }
}