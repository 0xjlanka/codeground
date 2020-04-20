/*
  # MIT License
  #
  # Copyright (c) 2020 Jitendra Lanka - https://github.com/0xjlanka
  #
  # Permission is hereby granted, free of charge, to any person obtaining a copy
  # of this software and associated documentation files (the "Software"), to deal
  # in the Software without restriction, including without limitation the rights
  # to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  # copies of the Software, and to permit persons to whom the Software is
  # furnished to do so, subject to the following conditions:

  # The above copyright notice and this permission notice shall be included in all
  # copies or substantial portions of the Software.

  # THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  # IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  # FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  # AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  # LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  # OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  # SOFTWARE.
*/

#include <stdio.h>
#include <usb.h>

#define VID 0x093a
#define PID1 0x2711
#define PID2 0x2712


static int beep_on(struct usb_dev_handle *handle)
{
    int ret;
    char data[4];
    data[0] = 4;
    data[1] = 0;
    ret = usb_control_msg(handle, 0xa1, 0x85, 0x0300, 1024, data, 2, 50000);
    if (ret < 0)
        goto err;
    data[0] = 0xE2;
    data[1] = 0;
    data[2] = 1;
    data[3] = 0;
    ret = usb_control_msg(handle, 0x21, 0x1, 0x0300, 1024, data, 4, 500000);
    if (ret < 0)
        goto err;
    data[0] = 0xE1;
    data[1] = 1;
    data[2] = 1;
    data[3] = 0;
    ret = usb_control_msg(handle, 0x21, 0x1, 0x0300, 1024, data, 4, 500000);
    if (ret < 0)
        goto err;

    return ret;
err:
    printf("%s: usb_control_msg failed with %d\n", __func__, ret);
    return ret;
}

static int beep_off(struct usb_dev_handle *handle)
{
    int ret;
    char data[4];
    data[0] = 4;
    data[1] = 0;
    ret = usb_control_msg(handle, 0xa1, 0x85, 0x0300, 1024, data, 2, 50000);
    if (ret < 0)
        goto err;
    data[0] = 0xE2;
    data[1] = 0;
    data[2] = 1;
    data[3] = 0;
    ret = usb_control_msg(handle, 0x21, 0x1, 0x0300, 1024, data, 4, 500000);
    if (ret < 0)
        goto err;
    data[0] = 0xE1;
    data[1] = 0;
    data[2] = 1;
    data[3] = 0;
    ret = usb_control_msg(handle, 0x21, 0x1, 0x0300, 1024, data, 4, 500000);
    if (ret < 0)
        goto err;

    return ret;
err:
    printf("%s: usb_control_msg failed with %d\n", __func__, ret);
    return ret;
}

static struct usb_device *device_init(void)
{
    struct usb_bus *usb_bus;
    struct usb_device *dev;

    usb_init();
    usb_find_busses();
    usb_find_devices();

    for (usb_bus = usb_busses; usb_bus; usb_bus = usb_bus->next) {
        for (dev = usb_bus->devices; dev; dev = dev->next) {
            if ((dev->descriptor.idVendor == VID) &&
                ((dev->descriptor.idProduct == PID1) ||
                (dev->descriptor.idProduct == PID2)))
                return dev;
        }
    }
    return NULL;
}

int main(int argc, char **argv)
{
    struct usb_device *usb_dev;
    struct usb_dev_handle *usb_handle;
    int ret = 0;

    usb_dev = device_init();
    if (usb_dev == NULL) {
        printf("Device not found\n");
        return -1;
    }

    usb_handle = usb_open(usb_dev);
    if (usb_handle == NULL) {
        printf("Unable to open USB device\n");
        goto out1;
    }
    usb_detach_kernel_driver_np(usb_handle, 0);
    ret = usb_claim_interface(usb_handle, 0);
    if (ret < 0) {
        printf("Unable to claim USB interface 0\n");
        goto out;
    }
    beep_on(usb_handle);
    printf("On\n");
    sleep(1);
    beep_off(usb_handle);
    printf("Off\n");
    sleep(1);
    beep_on(usb_handle);
    printf("On\n");
    sleep(1);
    beep_off(usb_handle);
    printf("Off\n");
out:
    usb_release_interface(usb_handle, 0);
out1:
    usb_close(usb_handle);
    return ret;
}
