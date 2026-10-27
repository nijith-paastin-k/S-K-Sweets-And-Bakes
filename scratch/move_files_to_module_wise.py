import os
import shutil

BASE = "app/src/main/java/com/skylink/sksweetsandbakes"

def ensure_dir(path):
    os.makedirs(os.path.join(BASE, path), exist_ok=True)

# 1. Create target directories
dirs = [
    "domain/auth", "domain/dashboard", "domain/cart", "domain/favourites", "domain/wishlist", "domain/orders",
    "data/auth", "data/dashboard", "data/cart", "data/favourites", "data/wishlist", "data/orders",
    "presentation/auth", "presentation/dashboard", "presentation/cart", "presentation/favourites", "presentation/wishlist", "presentation/orders"
]

for d in dirs:
    ensure_dir(d)

print("Directories prepared successfully.")
