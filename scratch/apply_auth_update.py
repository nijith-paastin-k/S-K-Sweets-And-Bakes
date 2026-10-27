import re

with open('app/src/main/java/com/skylink/sksweetsandbakes/feature/auth/AuthScreens.kt', 'r') as f:
    code = f.read()

# 1. Update EditAddressDialog
edit_address_old = re.search(r'@Composable\s+fun EditAddressDialog\(.*?\n\)', code, re.DOTALL)

print("Found EditAddressDialog:", edit_address_old is not None)

