USE [education]
GO
SET IDENTITY_INSERT [dbo].[TB_CODE] ON
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (1, NULL, N'A', N'A01', N'명칭부분', 0, NULL, N'A01', N'Y', 0, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (2, NULL, N'A01', N'A01AA', N'A01의 하위', 0, NULL, N'AA', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (3, NULL, N'A01', N'A01BB', N'A01의 하위2', 0, NULL, N'BB', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (4, NULL, N'', N'DOCUMENT', N'문서', 0, NULL, N'문서', N'Y', 0, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (5, NULL, N'DOCUMENT', N'DOCUMENTINSERT', N'입고완료(INSERT)', 0, NULL, N'입고완료', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (13, NULL, N'', N'EDUPLAN', N'교육계획', 0, NULL, N'EDUPLAN', N'Y', 0, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (14, NULL, N'EDUPLAN', N'EDUPLANPROCEED', N'진행방법', 0, NULL, N'PROCEED', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (15, NULL, N'EDUPLANPROCEED', N'EDUPLANPROCEEDM', N'대면', 0, NULL, N'M', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (16, NULL, N'EDUPLANPROCEED', N'EDUPLANPROCEEDN', N'비대면', 0, NULL, N'N', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (17, NULL, N'EDUPLAN', N'EDUPLANEVAL', N'평가방법', 0, NULL, N'EVAL', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (18, NULL, N'EDUPLANEVAL', N'EDUPLANEVALPROBLEM', N'문제', 0, NULL, N'PROBLEM', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (19, NULL, N'EDUPLANEVAL', N'EDUPLANEVALPRACTICE', N'실습', 0, NULL, N'PRACTICE', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (20, NULL, N'EDUPLAN', N'EDUPLANTYPE', N'교육유형', 0, NULL, N'TYPE', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (21, NULL, N'EDUPLANTYPE', N'EDUPLANTYPEDOC', N'문서', 0, NULL, N'DOC', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (22, NULL, N'EDUPLANTYPE', N'EDUPLANTYPECAPA', N'역량', 0, NULL, N'CAPA', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (23, NULL, N'EDUPLANTYPE', N'EDUPLANTYPEQUALI', N'직무적격성', 0, NULL, N'QUALI', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (24, NULL, N'EDUPLANTYPE', N'EDUPLANTYPEOUT', N'외부', 0, NULL, N'OUT', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (25, NULL, N'EDUPLAN', N'EDUPLANQUALIFIED', N'적격성정보', 0, NULL, N'QUALIFIED', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (26, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDQMS', N'QMS', 0, NULL, N'QMS', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (27, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDEDMS', N'EDMS', 0, NULL, N'EDMS', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (28, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDLX', N'LIMS/XRDMS', 0, NULL, N'LX', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (29, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDMES', N'MES', 0, NULL, N'MES', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (30, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDSAP', N'SAP', 0, NULL, N'SAP', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (31, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDETC', N'Etc.', 0, NULL, N'ETC', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (32, NULL, N'EDUPLANQUALIFIED', N'EDUPLANQUALIFIEDUNIT', N'단위', 0, NULL, N'UNIT', N'Y', 2, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (33, NULL, N'EDUPLANQUALIFIEDUNIT', N'EDUPLANQUALIFIEDUNITD', N'일', 0, NULL, N'D', N'Y', 3, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (34, NULL, N'EDUPLANQUALIFIEDUNIT', N'EDUPLANQUALIFIEDUNITM', N'월', 0, NULL, N'M', N'Y', 3, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (35, NULL, N'EDUPLANQUALIFIEDUNIT', N'EDUPLANQUALIFIEDUNITY', N'년', 0, NULL, N'Y', N'Y', 3, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (6, NULL, N'DOCUMENT', N'DOCUMENTLOAN', N'대출중(LOAN)', 1, NULL, N'대출중', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (7, NULL, N'DOCUMENT', N'DOCUMENTMOUNTAIN', N'아이언마운틴(MOUNTAIN)', 2, NULL, N'아이언마운틴', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (8, NULL, N'DOCUMENT', N'DOCUMENTDISCARD', N'폐기(DISCARD)', 3, NULL, N'폐기', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (9, NULL, N'DOCUMENT', N'DOCUMENTSUBSCRIBE', N'대출신청(SUBSCRIBE)', 4, NULL, N'대출신청', N'Y', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (10, NULL, N'DOCUMENT', N'DOCUMENTREADING', N'열람신청(READING)', 5, NULL, N'열람신청', N' ', 1, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[TB_CODE] ([id], [user_id], [parent_code], [code_name], [discription], [order_num], [plant_cd], [short_name], [use_yn], [depth_level], [sys_reg_date], [delete_at], [update_user_id], [update_date]) VALUES (11, NULL, N'DOCUMENT', N'DOCUMENTRETURN', N'대출반납(RETURN)', 6, NULL, N'대출반납', N'Y', 1, NULL, NULL, NULL, NULL)
GO
SET IDENTITY_INSERT [dbo].[TB_CODE] OFF
GO
