
# 20201015 ijplayer complie armv7a detail

```
====================
[*] check env armv7a
====================
FF_ARCH=armv7a
FF_BUILD_OPT=

--------------------
[*] make NDK standalone toolchain
--------------------
build on Darwin x86_64
ANDROID_NDK=/Users/yannischeng/Third_Projects/FFmpeg/android-ndk-r11c
IJK_NDK_REL=11.2.2725575
NDKr11.2.2725575 detected
HOST_OS=darwin
HOST_EXE=
HOST_ARCH=x86_64
HOST_TAG=darwin-x86_64
HOST_NUM_CPUS=8
BUILD_NUM_CPUS=16
Auto-config: --arch=arm
Copying prebuilt binaries...
Copying sysroot headers and libraries...
Copying c++ runtime headers and libraries...
Copying files to: /Users/yannischeng/Android_MySelf/ijkplayer/android/contrib/build/ffmpeg-armv7a/toolchain
Cleaning up...
Done.

--------------------
[*] check ffmpeg env
--------------------

--------------------
[*] configurate ffmpeg
--------------------
/Users/yannischeng/Android_MySelf/ijkplayer/android/contrib/build/ffmpeg-armv7a/toolchain/bin//arm-linux-androideabi-gcc
install prefix            /Users/yannischeng/Android_MySelf/ijkplayer/android/contrib/build/ffmpeg-armv7a/output
source path               .
C compiler                arm-linux-androideabi-gcc
C library                 bionic
host C compiler           gcc
host C library
ARCH                      arm (cortex-a8)
big-endian                no
runtime cpu detection     yes
ARMv5TE enabled           yes
ARMv6 enabled             yes
ARMv6T2 enabled           yes
VFP enabled               yes
NEON enabled              yes
THUMB enabled             yes
debug symbols             yes
strip symbols             yes
optimize for size         yes
optimizations             yes
static                    yes
shared                    no
postprocessing support    no
network support           yes
threading support         pthreads
safe bitstream reader     yes
texi2html enabled         yes
perl enabled              yes
pod2man enabled           yes
makeinfo enabled          yes
makeinfo supports HTML    no

External libraries:
xlib			   zlib

External libraries providing hardware acceleration（提供硬件加速的外部库）:

Libraries:
avcodec			   avformat		      swresample
avfilter		   avutil		      swscale

Programs:

Enabled decoders（启用的解码器）:
aac			       h263			      mp3adu			 mp3on4float		    vp9
aac_latm		   h264			      mp3adufloat		 vp6
flac			   hevc			      mp3float			 vp6f
flv			       mp3			      mp3on4			 vp8

Enabled encoders（启用的编码器）:
png

Enabled hwaccels:

Enabled parsers（启用的解析器）:
aac			       flac			      h264			 mpegaudio
aac_latm		   h263			      hevc			 vp9

Enabled demuxers（启用的多路分配器）:
aac			       flac			      hls			 mov			    mpegts
concat			   flv			      live_flv			 mp3			    mpegvideo
data			   hevc			      matroska			 mpegps			    webm_dash_manifest

Enabled muxers（启用的多路复用器）:
mov			       mp4

Enabled protocols（启用的协议）:
async			   ftp			          ijkio			         pipe			    tee
cache			   hls			          ijklongurl		     prompeg		    udp
data			   http			          ijkmediadatasource	 rtmp			    udplite
ffrtmphttp		   httpproxy		      ijksegment		     rtmpt
file			   ijkhttphook		      ijktcphook		     tcp

Enabled filters（启用的过滤器）:

Enabled bsfs:
aac_adtstoasc		   h264_mp4toannexb	      vp9_raw_reorder
extract_extradata	   null			          vp9_superframe_split

Enabled indevs:

Enabled outdevs:

```