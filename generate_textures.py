import os
from PIL import Image, ImageDraw

# Ensure directories exist
item_path = "real_life_mod/src/main/resources/assets/reallife/textures/item"
block_path = "real_life_mod/src/main/resources/assets/reallife/textures/block"

os.makedirs(item_path, exist_ok=True)
os.makedirs(block_path, exist_ok=True)

def create_texture(name, type_path, color_map, base_color=(0, 0, 0, 0)):
    img = Image.new('RGBA', (16, 16), base_color)
    draw = ImageDraw.Draw(img)

    for (x, y), color in color_map.items():
        draw.point((x, y), fill=color)

    img.save(os.path.join(type_path, f"{name}.png"))
    print(f"Generated {name}.png")

# Colors
GRAY = (100, 100, 100, 255)
DARK_GRAY = (60, 60, 60, 255)
WHITE = (240, 240, 240, 255)
RED = (200, 50, 50, 255)
BLUE = (50, 50, 200, 255)
GREEN = (50, 200, 50, 255)
BROWN = (139, 69, 19, 255)
ORANGE = (255, 165, 0, 255)
BLACK = (20, 20, 20, 255)
PINK = (255, 192, 203, 255)
YELLOW = (255, 255, 0, 255)
TRANSPARENT = (0, 0, 0, 0)

# --- ITEMS ---

# Dumbbell
dumbbell_pixels = {}
# Left weight
for x in range(2, 5):
    for y in range(5, 11):
        dumbbell_pixels[(x, y)] = BLACK
# Right weight
for x in range(11, 14):
    for y in range(5, 11):
        dumbbell_pixels[(x, y)] = BLACK
# Handle
for x in range(5, 11):
    dumbbell_pixels[(x, 7)] = GRAY
    dumbbell_pixels[(x, 8)] = GRAY
create_texture("dumbbell", item_path, dumbbell_pixels)

# Bandage
bandage_pixels = {}
# Roll shape
for x in range(4, 12):
    for y in range(4, 12):
        bandage_pixels[(x, y)] = WHITE
# Detail lines
for x in range(4, 12):
    bandage_pixels[(x, 4)] = GRAY
    bandage_pixels[(x, 11)] = GRAY
for y in range(4, 12):
    bandage_pixels[(4, y)] = GRAY
    bandage_pixels[(11, y)] = GRAY
bandage_pixels[(8, 8)] = (200, 200, 200, 255) # Center swirl hint
create_texture("bandage", item_path, bandage_pixels)

# Splint
splint_pixels = {}
# Two sticks
for y in range(2, 14):
    splint_pixels[(6, y)] = BROWN
    splint_pixels[(9, y)] = BROWN
# Ties
for y in [4, 8, 12]:
    for x in range(5, 11):
        splint_pixels[(x, y)] = WHITE
create_texture("splint", item_path, splint_pixels)

# Protein Shake
shake_pixels = {}
# Bottle
for x in range(6, 10):
    for y in range(4, 14):
        shake_pixels[(x, y)] = DARK_GRAY
# Lid
for x in range(5, 11):
    shake_pixels[(x, 3)] = BLUE
# Liquid indicator window
for y in range(6, 12):
    shake_pixels[(8, y)] = WHITE
create_texture("protein_shake", item_path, shake_pixels)

# Energy Bar
bar_pixels = {}
# Wrapper
for x in range(3, 13):
    for y in range(6, 10):
        bar_pixels[(x, y)] = ORANGE
# Text hint
bar_pixels[(5, 7)] = RED
bar_pixels[(6, 7)] = RED
bar_pixels[(9, 8)] = RED
create_texture("energy_bar", item_path, bar_pixels)

# Antibiotics
pill_bottle_pixels = {}
# Bottle
for x in range(6, 10):
    for y in range(6, 13):
        pill_bottle_pixels[(x, y)] = ORANGE
# Cap
for x in range(5, 11):
    pill_bottle_pixels[(x, 5)] = WHITE
# Label
for x in range(7, 9):
    for y in range(8, 11):
        pill_bottle_pixels[(x, y)] = WHITE
create_texture("antibiotics", item_path, pill_bottle_pixels)

# Soap
soap_pixels = {}
for x in range(4, 12):
    for y in range(6, 10):
        soap_pixels[(x, y)] = PINK
# Bubbles
soap_pixels[(5, 5)] = WHITE
soap_pixels[(12, 5)] = WHITE
create_texture("soap", item_path, soap_pixels)

# Energy Drink
can_pixels = {}
# Can body
for x in range(6, 10):
    for y in range(4, 14):
        can_pixels[(x, y)] = BLACK
# Green logo/bolt
can_pixels[(7, 6)] = GREEN
can_pixels[(8, 7)] = GREEN
can_pixels[(7, 8)] = GREEN
can_pixels[(8, 9)] = GREEN
# Top rim
for x in range(6, 10):
    can_pixels[(x, 4)] = GRAY
create_texture("energy_drink", item_path, can_pixels)

# Wiki Book
book_pixels = {}
# Cover
for x in range(4, 12):
    for y in range(3, 13):
        book_pixels[(x, y)] = GREEN
# Binding
for y in range(3, 13):
    book_pixels[(4, y)] = DARK_GRAY
# Text hint
book_pixels[(8, 6)] = YELLOW
book_pixels[(8, 8)] = YELLOW
book_pixels[(8, 10)] = YELLOW
create_texture("wiki_book", item_path, book_pixels)


# --- BLOCKS ---
# Blocks need full 16x16 textures generally

# Treadmill (generic metal with belt)
treadmill_pixels = {}
for x in range(0, 16):
    for y in range(0, 16):
        treadmill_pixels[(x, y)] = GRAY
# Belt
for x in range(4, 12):
    for y in range(0, 16):
        treadmill_pixels[(x, y)] = BLACK
create_texture("treadmill", block_path, treadmill_pixels)

# Bench Press (generic metal with padding)
bench_pixels = {}
for x in range(0, 16):
    for y in range(0, 16):
        bench_pixels[(x, y)] = GRAY
# Padding
for x in range(4, 12):
    for y in range(4, 12):
        bench_pixels[(x, y)] = RED
create_texture("bench_press", block_path, bench_pixels)
