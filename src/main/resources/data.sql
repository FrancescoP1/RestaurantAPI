INSERT INTO `menu_items` (`item_id`, `description`, `name`) VALUES
(1, 'item added through /populateData', 'item0'),
(2, 'item added through /populateData', 'item1'),
(3, 'item added through /populateData', 'item2'),
(4, 'item added through /populateData', 'item3'),
(5, 'item added through /populateData', 'item4'),
(6, 'item added through /populateData', 'item5'),
(7, 'item added through /populateData', 'item6'),
(8, 'item added through /populateData', 'item7'),
(9, 'item added through /populateData', 'item8'),
(10, 'item added through /populateData', 'item9'),
(11, NULL, 'item1');

--
-- Dumping data for table `tables`
--

INSERT INTO `tables` (`table_id`, `capacity`, `status`) VALUES
(1, 5, 'AVAILABLE'),
(2, 9, 'RESERVED'),
(3, 9, 'AVAILABLE'),
(4, 6, 'RESERVED'),
(5, 5, 'AVAILABLE'),
(6, 3, 'AVAILABLE'),
(7, 9, 'AVAILABLE'),
(8, 4, 'RESERVED'),
(9, 4, 'AVAILABLE'),
(10, 2, 'AVAILABLE');


--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `order_date`, `table_id`) VALUES
(3, NULL, 3),
(5, NULL, NULL),
(7, NULL, 2),
(8, '2022-12-14 21:01:43.899902', 2),
(9, '2022-12-14 21:05:07.617611', 4),
(10, '2022-12-14 21:28:39.880091', 8);

-- --------------------------------------------------------

-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_id`, `item_id`) VALUES
(3, 2),
(3, 3),
(7, 11),
(8, 3),
(8, 7),
(9, 3),
(9, 4),
(9, 7),
(10, 3),
(10, 4),
(10, 5),
(10, 7);

-- --------------------------------------------------------
