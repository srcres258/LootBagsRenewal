"""
This is a simple Python script for generating a progress bar texture used for bag opener block.

It depends on PIL (Python Imaging Library).
"""


from PIL import Image, ImageDraw


if __name__ == '__main__':
    image = Image.new('RGBA', (256, 256), color=(0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    for x in range(162):
        draw.rectangle([x, 0, x, 5], fill=(int((161 - x) / 162 * 255), int(x / 161 * 255), 0, 255))
    image.save('progress_bar.png')