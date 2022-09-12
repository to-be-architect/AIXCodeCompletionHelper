package com.github.aixcode.constant

val codeMap = hashMapOf<String, String>(
    "rsd" to """
    rsd := rocket.NewRSD(ctx, onetable).SetRowKeyList([]string{"group_id", "spu_sim_id", "product_id", "shop_id", "check_status"})
    """.trimIndent(),

    "cql" to """
    cql := alpha.CQL{}
	cql.Select("group_id", "check_status",
		"spu_sim_id",
		"product_id",
		"shop_id",
		"date",
		"first_cid", "second_cid", "third_cid", "leaf_cid",
		"pay_amt / 100 as pay_amt", // 成交金额
		"pay_ucnt",                 // 成交人数
		"pay_combo_cnt",            // 成交件数
		"pay_cnt",                  // 成交订单数
		"new_user_pay_amt / 100 as new_user_pay_amt",               // 新客成交金额
		"live_pay_amt / 100 as live_pay_amt",                       // 直播成交金额
		"video_pay_amt / 100 as video_pay_amt",                     // 短视频成交金额
		"self_employed_pay_amt / 100 as self_employed_pay_amt",     // 自营成交金额
		"bring_goods_pay_amt / 100 as bring_goods_pay_amt",         // 带货成交金额
		"product_show_cnt",                                         // 曝光次数
		"product_click_cnt",                                        // 点击次数
		"refund_amt / 100 as refund_amt",                           // 退款金额
		"pay_amt / pay_combo_cnt / 100 AS avg_price",               // 件单价
		"product_click_cnt / product_show_cnt AS click_show_ratio", // 点击曝光转化率
		"pay_cnt / product_click_cnt AS pay_click_ratio",           // 点击成交转化率
		"pay_amt / product_show_cnt * 10 as gpm",                   // 千次曝光转化金额
	).From(oneservice_table.SpuInsightGroupProductStatsTableName).
		AddWhereInValue(alpha.LONG, "spu_sim_id", searchSpuIds).
		AddWhereValueWithOperator(convert.ToString(groupId), alpha.EQUAL, alpha.LONG, "group_id").
		AddWhereValueWithOperator(oneservice_table.TableValidStatus, alpha.EQUAL, alpha.LONG, "check_status").
		AddWhereAndValue(util.GetDateTypeWhere(req.BeginDate, req.EndDate, req.DateType)).
		//AddWhereValueWithOperator(oneservice_table.TableValueZero, alpha.GREATER_THAN, alpha.LONG, "pay_amt", "refund_amt").
		SetSortInfoList(isAsc, sortField, "product_id").SetPageInfo(pageNo, pageSize)
    """.trimIndent(),
)