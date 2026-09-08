[*] Progress: 10527/10527

[*] Fuzzing completed, generating report...


### Crash Location Aggregation Table （Skia）

| Crash Location | Sample Files Triggering the Crash (up to 3 per location) |
|----------|--------------------------------------------|
| `ASan heap-buffer-overflow in RefBaselineABCDtoRGB` | `/home/***/report/heap_overflow_RefBaselineABCDtoRGB.png` |
| `ASan stack-buffer-overflow in dng_interleave_task::Start` | `/home/***/report/stack_dng_interleave_task_Start.png`, `all_crashes/id:000006,sig:06,src:019839,time:483971080,execs:35947315,op:quick,pos:31`, `all_crashes/id:000006,sig:06,src:019965,time:495362512,execs:37610082,op:quick,pos:31,val:+3` ... *(807 in total)* |
| `external/dng_sdk/source/dng_linearization_info.cpp:746:9` | `/home/***/report/461782921.png` |
| `external/dng_sdk/source/dng_lossless_jpeg_shared.cpp:1935:48` | `/home/***/report/462390721-more.png`, `all_crashes/id:000003,sig:06,src:019813,time:472338971,execs:33575777,op:quick,pos:14456,val:+2`, `all_crashes/id:000004,sig:06,src:019696,time:481007691,execs:36147048,op:quick,pos:14457` ... *(380 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:275:22` | `/home/***/report/dng_misc_opcodes_275.png`, `all_crashes/id:000000,sig:06,src:005343,time:457680099,execs:34522245,op:quick,pos:13086,val:+6`, `all_crashes/id:000000,sig:06,src:005384,time:466630552,execs:33365837,op:quick,pos:13002` ... *(2626 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:276:22` | `/home/***/report/456422800.png`, `/home/***/report/dng_opcode_MapTable_ProcessArea.png`, `/home/***/report/dng_misc_opcodes_276.png` ... *(1784 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:565:32` | `/home/***/report/dng_misc_opcodes_565.png` |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:792:32` | `/home/***/report/449448549.webp`, `all_crashes/id:000004,sig:06,src:013834,time:462414081,execs:35774773,op:quick,pos:13022`, `all_crashes/id:000020,sig:06,src:013872,time:589041270,execs:43213294,op:quick,pos:13022,val:+2` ... *(208 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:827:26` | `all_crashes/id:000142,sig:06,src:015163,time:1799832692,execs:154010339,op:havoc,rep:4`, `all_crashes/id:000158,sig:06,src:025220,time:2194699412,execs:198120208,op:havoc,rep:1`, `all_crashes/id:000164,sig:06,src:025093,time:2434371274,execs:223015670,op:havoc,rep:2` ... *(6 in total)* |
| `external/dng_sdk/source/dng_read_image.cpp:2455:43` | `/home/***/report/467965812.png`, `all_crashes/id:000004,sig:06,src:017771,time:469342506,execs:33699022,op:quick,pos:165,val:+2`, `all_crashes/id:000004,sig:06,src:017828,time:470877083,execs:35706681,op:quick,pos:165` ... *(124 in total)* |
| `external/dng_sdk/source/dng_read_image.cpp:3673:43` | `/home/***/report/467888081.png`, `all_crashes/id:000000,sig:06,src:019378,time:467186175,execs:36758508,op:havoc,rep:12`, `all_crashes/id:000003,sig:06,src:019772,time:470244671,execs:35203094,op:quick,pos:156` ... *(370 in total)* |
| `external/dng_sdk/source/dng_reference.cpp:2897:18` | `/home/***/report/456380811.png`, `/home/***/report/crash-dng_reference.png` |
| `external/dng_sdk/source/dng_stream.cpp:1050:10` | `/home/***/report/470580610.png`, `all_crashes/id:000064,sig:06,src:023174,time:979346807,execs:68203643,op:havoc,rep:3`, `all_crashes/id:000068,sig:06,src:015077,time:1045860920,execs:74741577,op:havoc,rep:2` ... *(100 in total)* |
| `external/dng_sdk/source/dng_stream.cpp:1051:10` | `/home/***/report/470582070.png`, `all_crashes/id:000019,sig:06,src:014236,time:561402367,execs:38732928,op:havoc,rep:4`, `all_crashes/id:000056,sig:06,src:009824,time:907626133,execs:60327938,op:havoc,rep:2` ... *(63 in total)* |
| `external/dng_sdk/source/dng_string.cpp:951:28` | `/home/***/report/470574979.png`, `all_crashes/id:000001,sig:06,src:003640,time:458071993,execs:33478365,op:havoc,rep:15`, `all_crashes/id:000002,sig:06,src:003640,time:458072164,execs:33478387,op:havoc,rep:15` ... *(2071 in total)* |
| `external/dng_sdk/source/dng_utils.cpp:1456:59` | `/home/***/report/470577223.png`, `all_crashes/id:000048,sig:06,src:022247,time:848690792,execs:56985078,op:quick,pos:177,val:+3`, `all_crashes/id:000052,sig:06,src:022327,time:915212331,execs:63063479,op:havoc,rep:8` ... *(1983 in total)* |

### No crash samples were found in androidfw. 
