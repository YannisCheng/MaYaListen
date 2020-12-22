# 媒体协议

## 流媒体概念

**维基百科**：

[Streaming media](https://en.wikipedia.org/wiki/Streaming_media)

`流媒体（Streaming media）` 是一种多媒体，在提供商交付的同时，它经常被最终用户接收并且呈现给最终用户。

`Stream（流）` 这个 `动词` 是指以这种方式 `传递或获取` 媒体的 `过程`。

`Streaming（流）` 是指媒体的 `传递方法（delivery method）`，而不是指媒体本身。

Internet上的流内容存在变数。例如，其Internet连接缺少 `足够带宽` 的用户可能会遇到停止，滞后或内容缓冲缓慢的情况；缺乏 `兼容` 的硬件或软件系统的用户可能无法流式传输某些内容。

`实时流式传输` 是 `实时传输Internet内容`，就像实时电视的电视信号通过无线电波广播内容一样。`实时Internet流媒体` 需要：

1. 一种形式的 `源媒体（a form of source media）`（例如摄像机，音频接口，屏幕捕获软件）
2. 将内容数字化的 `编码器`（an encoder to digitize the content）
3. `媒体发布者`（a media publisher）
4. `内容分发网络进行分发`（a content delivery network to distribute）
5. `分发内容`（deliver the content）

实时流媒体无需在始发点记录，尽管经常需要记录。

`Streaming（流）`是文件下载的一种 `替代（alternative）` 方法，该过程是最终用户在观看或收听文件之前获取内容的 `整个文件（entire file）` 的过程。通过 `流传输`，最终用户可以使用其 `媒体播放器` 在传输整个文件之前开始播放数字视频或数字音频内容。

术语 `流媒体` 可以应用于 `视频` 和 `音频` 以外的媒体，例如 `实时隐藏字幕`，`自动收录机磁带` 和 `实时文本`，它们都被视为 `流文本`。

**通俗说法**

`流媒体（streaming media）` 是指采用 `流式传输技术` 在 `网络` 上 `连续实时播放` 的 `媒体格式`，如音频、视频或多媒体文件，采用流媒体技术使得 `数据包` 得以像 `流水` 一样发送, 如果没有流媒体技术, 那么我们就要像以前用迅雷下电影一样, 下载整个影片才能观看。

常用的协议: `HLS`、`RTMP`、`HDS`、`DASH`（之前技术的组合）协议。

## 发展

---

历史

1980年代后期到1990年代，与流传输有关的主要技术问题是拥有足够的CPU和总线带宽以支持所需的数据速率，实现所需的实时计算性能以防止缓冲区不足并实现内容的流畅流传输
The primary technical issues related to streaming were having enough CPU and bus bandwidth to support the required data rates, achieving real-time computing performance required to prevent buffer underrun and enable smooth streaming of the content

1990年代中期，计算机网络仍然受到限制

由于未压缩媒体的带宽要求过高，实际的流媒体只有随着数据压缩的发展才有可能实现。对于未经压缩的CD音频，使用脉冲编码调制（PCM）编码的原始数字音频需要1.4 Mbit / s的带宽，而对于SD视频，原始数字视频需要168 Mbit / s的带宽，对于FHD视频需要的带宽超过1000 Mbit / s

支持实际流媒体的最重要的压缩技术是离散余弦变换（DCT），一种有损压缩形式，由纳西尔·艾哈迈德（Nasir Ahmed）于1972年提出。1973年，他与德克萨斯大学的T. Natarajan和K. R. Rao合作开发了该算法。

DCT算法是1988年第一种实用的视频编码格式H.261的基础。它最初用于在线视频会议。随后是更流行的基于DCT的视频编码标准，最著名的是从1991年开始的MPEG视频格式。

1987年，萨里大学的J.P.Princen，A.W.Johnson和A.B.Bradley将DCT算法应用于改进的离散余弦变换（MDCT）。MDCT算法是1994年推出的MP3音频格式的基础，尤其是1999年推出的使用更广泛的 高级音频编码Advanced Audio Coding （AAC）格式。

在1990年代初期，“流”被用作对视频点播以及后来在IP网络上的实时视频的更好描述。它首先由Starlight Networks用于视频流传输，而Real Networks用于音频流传输。此类视频以前曾被误称“存储并转发视频”。

在1990年代末和2000年代初，随着Internet越来越商业化，标准协议和格式（例如TCP / IP，HTTP，HTML）的使用也越来越多，这导致对该领域的投资大量涌入。

业务发展

第一个商业流媒体产品出现在1992年底，并命名为StarWorks。通过StarWorks，可以在公司以太网上随机访问点播MPEG-1全动态视频。 Starworks来自Starlight Networks，后者还率先在以太网上通过Hughes Network Systems通过卫星通过Internet协议通过Internet协议进行实时视频流传输。

术语“流媒体战争”是为了讨论视频流媒体服务之间竞争的新时代而开发的，例如Netflix，Amazon Prime Video，Hulu，HBO Max，Disney+和Apple TV +


公众使用流媒体

媒体流可以“实时（live）”或“按需（on demand）”流式传输。

- 实时流（Live streams）通常通过称为“真实流（true streaming）”的手段来提供。真正的流式传输将信息直接发送到计算机或设备，而无需将文件保存到硬盘。
- 按需流媒体（On-demand streaming）通过一种称为渐进流媒体（progressive streaming）或渐进式下载（progressive download）的方式提供。渐进式流传输将文件保存到硬盘，然后从该位置播放。

按需流通常会保存到硬盘和服务器上较长时间，而实时流仅一次可用（例如，在足球比赛期间）


---

带宽和存储（<broadband width>Bandwidth and storage）

- 标清（Standard definition）视频流，建议在不经历缓冲或跳跃的情况下建议使用2Mbit/s或更高的宽带速度，尤其是实况视频；
- 高清（High Definition）内容，建议速率为5Mbit/s；
- 超高（Ultra-High Definition）清内容，建议速率为9Mbit/s。


Streaming media storage size is calculated from the streaming bandwidth and length of the media(for a single user and file)，公式为：
storage size in megabytes is equal to length (in seconds) × bit rate (in bit/s) / (8 × 1024 × 1024)
存储大小（以Megabytes为单位）= 长度（以s为单位）×比特率（以bit/s为单位）/（8×1024×1024）。

例如：时长一个小时，以300kbit/s编码的数字视频（这是2005年的典型宽带视频，通常以320×240像素的窗口大小进行编码）将是：（3,600s×300,000bit/s）/ （8×1024×1024）需要大约128MB的存储空间。

不同协议所需的服务带宽是不同的。

---

协议

使用音视频编码格式（coding format）压缩（compressed）音视频流（stream），以减小文件大小。编码的音频流和视频流被组合（assembled）在一个容器（container）“位流（bitstream）”中，例如MP4，FLV，WebM，ASF或ISMA。

使用传输协议，将比特流从流服务器传输到流客户端。

在2010年代，出现了诸如Apple的HLS，Microsoft的Smooth Streaming，Adobe的HDS和非专有格式（non-proprietary formats）（例如MPEG-DASH）之类的技术，以实现通过HTTP的自适应比特率流传输（adaptive bitrate streaming over HTTP），以替代使用专有传输协议（proprietary transport protocols）。


通常，流传输协议（streaming transport protocol）用于将视频从活动场所（event venue）发送到“云”代码转换服务（transcoding service）和CDN，然后CDN使用基于HTTP的传输协议（HTTP-based transport protocols）将视频分发给各个家庭和用户。流客户端（最终用户）可以使用诸如MMS或RTSP的控制协议与流服务器交互（interact）。

服务器与用户之间的交互质量（quality of the interaction）取决于流服务的工作量（workload of the streaming service）；

随着越来越多的用户尝试访问服务，除非有足够的带宽或主机使用足够的代理网络，否则质量会受到更大的影响。
部署流服务器集群（Deploying clusters of streaming servers）的方法是：区域服务器（regional servers）分布在整个网络（spread across the network）中，由单个（singular）中央服务器管理，中央服务器包含所有媒体文件的副本以及区域服务器的IP地址。 然后，该中央服务器使用负载平衡（load balancing）和调度算法（scheduling algorithms）将用户重定向（redirect）到能够容纳（capable of accommodating）用户的附近区域服务器。
这种方法还允许中央服务器在需要时使用FFMpeg库向用户以及区域服务器提供流数据，从而要求中央服务器具有强大的数据处理能力（powerful data-processing）和巨大的存储能力（immense storage capabilities）。作为回报，流媒体骨干网（streaming backbone network）上的工作负载得到平衡（balanced）和减轻（alleviated），从而实现了最佳的流媒体质量（optimal streaming quality） 。


---

Protocol challenges

数据报协议（Datagram protocols）（例如用户数据报协议（User Datagram Protocol ，UDP））将媒体流作为一系列小数据包发送。 这是简单而有效的； 但是，协议中没有机制（mechanism）来保证（guarantee）传递。 接收应用程序可以使用纠错技术（error correction techniques）来检测（detect）丢失（loss）或损坏（corruption）并恢复数据。 如果数据丢失，则流可能会丢失。 实时流协议（Real-time Streaming Protoco，RTSP），实时传输协议（Real-time Transport Protocol，RTP）和实时传输控制协议（Real-time Transport Control Protocol ，RTCP）是专门设计用于通过网络流媒体的。 RTSP运行在各种传输协议上，而后两个则建立在UDP之上。

似乎兼顾了使用标准Web协议的优点和用于流甚至直播内容的能力的另一种方法是自适应比特率流（adaptive bitrate streaming）。HTTP自适应比特率流传输（HTTP adaptive bitrate streaming）基于HTTP渐进式下载（HTTP progressive download），但是与以前的方法相反，此处的文件非常小，因此可以将它们与数据包的流进行比较，就像使用RTSP和RTP的情况一样。但是，他们通过超时和重试系统来完成此任务，这使它们的实现更加复杂。 这也意味着，当网络上发生数据丢失时，媒体流将停顿，而协议处理程序将检测到丢失并重新传输丢失的数据。 客户可以通过缓冲显示数据来最大程度地减少这种影响。 虽然在视频点播场景中可以接受由于缓冲引起的延迟，但是如果由缓冲引起的延迟超过200毫秒，则交互式应用程序（例如视频会议）的用户将遭受保真度的损失。

单播协议（Unicast protocols）将媒体流的单独副本从服务器发送到每个收件人。单播是大多数Internet连接的规范，但是当许多用户想要同时观看同一电视节目时，单播就无法很好地扩展。开发了多播协议（Multicast protocols）以减少由于重复数据流导致的服务器/网络负载，当许多接收者独立接收单播内容流时，重复数据流就会出现。这些协议将单个流从源发送到一组收件人。根据网络基础结构和类型，多播传输可能是可行的，也可能是不可行的。多播的一个潜在缺点是视频点播功能的丧失。广播或电视材料的连续播放通常会阻止收件人控制播放的能力。但是，可以通过诸如缓存服务器，数字机顶盒和缓冲媒体播放器之类的元素来缓解此问题。

IP组播（IP Multicast）提供了一种将单个媒体流发送到计算机网络上的一组收件人的方法。多播协议（通常是Internet组管理协议）用于管理向LAN上的接收者组的多播流的传递。部署IP多播的挑战之一是，局域网之间的路由器和防火墙必须允许目的地为多播组的数据包通过。如果提供内容的组织对服务器和收件人（例如，教育，政府和公司内部网）之间的网络具有控制权，则可以使用路由协议（例如协议独立多播）将流内容传递到多个局域网段。与大量传送内容一样，多播协议所需的能源和其他资源也要少得多，可靠的多播协议（类似于广播的协议）的广泛引入及其在可能的情况下的优先使用是一项重大的生态和经济挑战。对等（P2P）协议安排在计算机之间发送预记录的流。这样可以防止服务器及其网络连接成为瓶颈。但是，这会引发技术，性能，安全性，质量和业务问题。

-------

视频编码格式

数据压缩

网络带宽：是指在单位时间（一般指的是1秒钟）内能传输的数据量。


---

## 协议与格式

1974年，离散余弦变换（DCT）算法是后来启用实际视频流的最重要的数据压缩技术。
1988年，H.261视频编码标准采用离散余弦变换（DCT）视频压缩，使其成为第一实际视频编码格式。和它被用于在线视频会议。此后遵循的所有MPEG视频编码标准（包括MPEG-1，MPEG-2 Video，H.264 / MPEG-4 AVC和HEVC）都已使用DCT视频压缩。
1998年，引入了MPEG-4，一种定义音频和视频（AV）数字数据压缩的方法。
1999年，Microsoft在Windows Media Player 6.4中引入了流功能。苹果在其QuickTime 4应用程序中引入了流媒体格式
2009年十一月,苹果首先引入了HLS（HTTP实时流），这是一种基于HTTP的自适应比特率流通信协议。
2011年，通过HTTP的动态自适应流传输（通过传统的HTTP Web服务器提供的Internet上高质量的媒体内容流传输）已成为国际标准草案。

## HLS(HTTP Live Streaming)协议


2009年十一月,苹果首先引入了HLS（HTTP实时流），这是一种基于HTTP的自适应比特率流通信协议。移动端支持良好, 现在已经成为移动端H5直播的主要技术,。
它的工作原理是把整个流分成一个个小的基于HTTP的文件来下载，每次只下载一些。当媒体流正在播放时，客户端可以选择从许多不同的备用源中以不同的速率下载同样的资源，允许流媒体会话适应不同的数据速率。在开始一个流媒体会话时，客户端会下载一个包含元数据的extended M3U (m3u8)playlist文件，用于寻找可用的媒体流。但是HLS由于使用的HTTP协议传输数据(80端口)，不会遇到被防火墙屏蔽的情况.

## HDS(Http Dynamic Streaming)协议

由Adobe公司模仿 `HLS协议` 提出的另一个基于 `Http` 的流媒体传输协议。其模式跟 HLS类似，但是又要比HLS协议复杂一些，也是 `索引文件` 和 `媒体切片文件` 结合的下载方式。

## MSS协议

## DASH(Dynamic Adaptive Streaming over HTTP)协议

是国际标准组 `MPEG` 2014年推出的技术标准, 主要目标是形成IP网络承载单一格式的流媒体并提供高效与高质量服务的统一方案, 解决 `多制式` 传输方案(`HTTP Live Streaming`, `Microsoft Smooth Streaming`, `HTTP Dynamic Streaming`)并存格局下的存储与服务能力浪费、运营高成本与复杂度、系统间互操作弱等问题。

原理：`DASH` 是基于 `HTTP` 的动态自适应的比特率流技术，使用的传输协议是 `TCP`。 和 `HLS` , `HDS` 技术类似， 都是把视频 `分割` 成一小段一小段， 通过 `HTTP` 协议进行传输，客户端得到之后进行播放；不同的是MPEG-DASH支持MPEG-2 TS、MP4等多种格式, 可以将视频按照多种编码切割, 下载下来的媒体格式既可以是ts文件也可以是mp4文件, 所以当客户端加载视频时, 按照当前的网速和支持的编码加载相应的视频片段进行播放.

[前端要懂的视频知识DASH协议（建议收藏](https://blog.csdn.net/qiwoo_weekly/article/details/93149710)

[DASH简介及使用方法(FFmpeg, MP4Box-YouTube调研:Representation)](https://blog.csdn.net/yue_huang/article/details/78466537)

[自适应流媒体传输（一）——DASH媒体内容的生成](https://blog.csdn.net/nonmarking/article/details/50397153?utm_medium=distribute.pc_relevant.none-task-blog-baidulandingword-6&spm=1001.2101.3001.4242)

[自适应流媒体传输（二）——为什么要使用fragmented MP4](https://blog.csdn.net/nonmarking/article/details/53439481)

[自适应流媒体传输（三）——和TS格式说再见](https://blog.csdn.net/nonmarking/article/details/54604396?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control)

[自适应流媒体传输（四）——深入理解MPD](https://blog.csdn.net/nonmarking/article/details/85714099)

[自适应流媒体传输（五）——正确认识码率切换](https://blog.csdn.net/nonmarking/article/details/86351147?utm_medium=distribute.pc_relevant.none-task-blog-title-2&spm=1001.2101.3001.4242)

## 概念精讲

[Dash Hls SmoothStreaming manifest 文件字段解释](https://blog.csdn.net/beautyfuel/article/details/53464705)



